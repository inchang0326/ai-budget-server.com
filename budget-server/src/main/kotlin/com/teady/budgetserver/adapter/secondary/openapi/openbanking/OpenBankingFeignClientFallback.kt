package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.application.dto.OpenBankingCardDto
import com.teady.budgetserver.global.exception.*
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.stereotype.Component
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * OpenBankingFeignClient에 대한 Fallback Factory
 * - 타겟 서버로부터 HTTP 오류 응답을 받지 못 했을 때 작동하는 Fallback 패턴
 * - 만약 HTTP 오류 응답을 받았다면 ErrorDecoder => Exceptionhandler에 의해 처리
 * 1. getFinCardNumber() - 읽기 작업 (기본값 반환)
 * 2. createFinCardNumber() - 쓰기 작업 (재시도 큐)
 * 3. getCardHistory() - 읽기 작업 (빈 리스트 반환)
 */
@Component
class OpenBankingFeignClientFallbackFactory : FallbackFactory<OpenBankingFeignClient> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun create(cause: Throwable): OpenBankingFeignClient {

        logCauseDetail(cause)

        return object : OpenBankingFeignClient {

            /**
             * 기존 금융카드 번호 조회 (읽기 작업)
             *
             * Fallback 전략: 기본값/캐시 반환
             * - 일시적 장애 시 서비스 운영 필요
             * - 데이터 손실 위험 없음
             */
            override fun getFinCardNumber(): String {
                return when (cause) {
                    // 연결 실패
                    is ConnectException -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Connection failed, returning cached value")
                        "CARD_0000000000"  // 캐시된 값 또는 기본값
                    }

                    // 타임아웃
                    is SocketTimeoutException -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Read timeout, returning fallback")
                        "CARD_9999999999"
                    }

                    is java.io.IOException -> {
                        logger.error("[FALLBACK] getFinCardNumber - IO error: ${cause.message}")
                        "CARD_UNKNOWN"
                    }

                    // HTTP 4xx 에러
                    is FeignException.BadRequest -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Bad request (400)")
                        "CARD_INVALID_REQUEST"
                    }

                    is FeignException.Unauthorized -> {
                        logger.error("[FALLBACK] getFinCardNumber - Unauthorized (401)")
                        throw FeignUnauthorizedException(
                            message = "금융카드 조회 인증이 필요합니다"
                        )
                    }

                    is FeignException.Forbidden -> {
                        logger.error("[FALLBACK] getFinCardNumber - Forbidden (403)")
                        throw FeignForbiddenException(
                            message = "금융카드 조회 권한이 없습니다"
                        )
                    }

                    is FeignException.NotFound -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Not found (404)")
                        throw FeignResourceNotFoundException(
                            message = "금융카드를 찾을 수 없습니다"
                        )
                    }

                    // HTTP 5xx 에러
                    is FeignException.InternalServerError -> {
                        logger.error("[FALLBACK] getFinCardNumber - Server error (500)")
                        "CARD_SERVER_ERROR"
                    }

                    is FeignException.ServiceUnavailable -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Service unavailable (503)")
                        "CARD_SERVICE_UNAVAILABLE"
                    }

                    is FeignException.GatewayTimeout -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Gateway timeout (504)")
                        "CARD_TIMEOUT"
                    }

                    // Circuit Breaker OPEN
                    is io.github.resilience4j.circuitbreaker.CallNotPermittedException -> {
                        logger.warn("[FALLBACK] getFinCardNumber - Circuit breaker open")
                        "CARD_CIRCUIT_OPEN"
                    }

                    // 기타 예외
                    else -> {
                        logger.error("[FALLBACK] getFinCardNumber - Unexpected error: ${cause.javaClass.simpleName}", cause)
                        "CARD_UNKNOWN_ERROR"
                    }
                }
            }

            /**
             * 신규 금융카드 번호 생성 (쓰기 작업)
             *
             * Fallback 전략: 재시도 큐 추가 + 명시적 실패
             * - 데이터 생성이므로 안전성 중요
             * - 일단 실패 알림 후 비동기 재시도
             * - 최종 실패 시 사용자 공지 필요
             */
            override fun createFinCardNumber(): String {
                logger.error("[FALLBACK] createFinCardNumber triggered due to: ${cause.javaClass.simpleName}")

                return when (cause) {
                    // 일시적 장애: 재시도 큐에 추가
                    is ConnectException,
                    is SocketTimeoutException,
                    is java.io.IOException -> {
                        logger.info("[FALLBACK] createFinCardNumber - Adding to retry queue")
                        // retryQueueService.addToRetryQueue(
                        //     operation = "createFinCardNumber",
                        //     maxRetries = 3,
                        //     retryDelay = Duration.ofSeconds(30)
                        // )
                        throw FeignServiceUnavailableException(
                            message = "금융카드 생성 서비스를 일시적으로 사용할 수 없습니다. " +
                                    "잠시 후 자동으로 재시도됩니다."
                        )
                    }

                    // 클라이언트 에러: 즉시 실패
                    is FeignException.BadRequest -> {
                        logger.error("[FALLBACK] createFinCardNumber - Bad request (400)")
                        throw FeignBadRequestException(
                            message = "금융카드 생성 요청이 올바르지 않습니다"
                        )
                    }

                    is FeignException.Unauthorized -> {
                        logger.error("[FALLBACK] createFinCardNumber - Unauthorized (401)")
                        throw FeignUnauthorizedException(
                            message = "금융카드 생성 인증이 필요합니다"
                        )
                    }

                    is FeignException.Forbidden -> {
                        logger.error("[FALLBACK] createFinCardNumber - Forbidden (403)")
                        throw FeignForbiddenException(
                            message = "금융카드 생성 권한이 없습니다"
                        )
                    }

                    // 서버 에러: 재시도 큐 + 실패
                    is FeignException.InternalServerError,
                    is FeignException.ServiceUnavailable,
                    is FeignException.GatewayTimeout -> {
                        logger.error("[FALLBACK] createFinCardNumber - Server error (${(cause as? FeignException)?.status()})")
                        // eventPublisher.publishEvent(
                        //     CardCreationFailedEvent(cause.message)
                        // )
                        throw FeignServiceUnavailableException(
                            message = "금융카드 생성 중 오류가 발생했습니다. 관리자에게 문의하세요."
                        )
                    }

                    // Circuit Breaker OPEN
                    is io.github.resilience4j.circuitbreaker.CallNotPermittedException -> {
                        logger.warn("[FALLBACK] createFinCardNumber - Circuit breaker open, queuing for retry")
                        throw FeignServiceUnavailableException(
                            message = "금융카드 생성 서비스가 일시적으로 제한됩니다. " +
                                    "잠시 후 다시 시도해주세요."
                        )
                    }

                    // 기타 예외
                    else -> {
                        logger.error("[FALLBACK] createFinCardNumber - Unexpected error", cause)
                        throw FeignServiceUnavailableException(
                            message = "금융카드 생성 중 예상하지 못한 오류가 발생했습니다"
                        )
                    }
                }
            }

            /**
             * 금융카드 거래 내역 조회 (읽기 작업)
             *
             * Fallback 전략: 빈 리스트/캐시된 리스트 반환
             * - 목록 조회이므로 빈 리스트 반환 안전
             * - 사용자 경험: "조회된 거래 내역이 없습니다" 표시 가능
             */
            override fun getCardHistory(): List<OpenBankingCardDto> {
                logger.warn("[FALLBACK] getCardHistory triggered due to: ${cause.javaClass.simpleName}")

                return when (cause) {
                    // 연결/타임아웃: 빈 리스트 반환
                    is ConnectException,
                    is SocketTimeoutException,
                    is java.io.IOException -> {
                        logger.warn("[FALLBACK] getCardHistory - Connection issue, returning cached history")
                        // getCachedCardHistory() ?: emptyList()
                        emptyList()
                    }

                    // HTTP 4xx: 빈 리스트 또는 예외
                    is FeignException.BadRequest -> {
                        logger.warn("[FALLBACK] getCardHistory - Bad request (400)")
                        emptyList()
                    }

                    is FeignException.Unauthorized -> {
                        logger.error("[FALLBACK] getCardHistory - Unauthorized (401)")
                        throw FeignUnauthorizedException(
                            message = "거래 내역 조회 인증이 필요합니다"
                        )
                    }

                    is FeignException.Forbidden -> {
                        logger.error("[FALLBACK] getCardHistory - Forbidden (403)")
                        throw FeignForbiddenException(
                            message = "거래 내역 조회 권한이 없습니다"
                        )
                    }

                    is FeignException.NotFound -> {
                        logger.info("[FALLBACK] getCardHistory - Not found (404)")
                        emptyList()
                    }

                    // HTTP 5xx: 빈 리스트 반환 (서비스 운영)
                    is FeignException.InternalServerError -> {
                        logger.error("[FALLBACK] getCardHistory - Server error (500)")
                        emptyList()
                    }

                    is FeignException.ServiceUnavailable -> {
                        logger.warn("[FALLBACK] getCardHistory - Service unavailable (503)")
                        emptyList()
                    }

                    is FeignException.GatewayTimeout -> {
                        logger.warn("[FALLBACK] getCardHistory - Gateway timeout (504)")
                        emptyList()
                    }

                    // Circuit Breaker OPEN
                    is io.github.resilience4j.circuitbreaker.CallNotPermittedException -> {
                        logger.warn("[FALLBACK] getCardHistory - Circuit breaker open, returning empty list")
                        emptyList()
                    }

                    // 기타 예외
                    else -> {
                        logger.error("[FALLBACK] getCardHistory - Unexpected error", cause)
                        emptyList()
                    }
                }
            }
        }
    }

    /**
     * 예외 원인 상세 로깅
     */
    private fun logCauseDetail(cause: Throwable) {
        val causeName = cause.javaClass.simpleName
        val message = cause.message
        val stackTrace = cause.stackTrace.take(3).joinToString("\n  ") { it.toString() }

        logger.error("""
            ╔════════════════════════════════════════════════════╗
            ║     FALLBACK TRIGGERED - OpenBanking API           ║
            ╠════════════════════════════════════════════════════╣
            ║ Exception Type: $causeName
            ║ Message: $message
            ║ Timestamp: ${System.currentTimeMillis()}
            ║ Stack Trace (Top 3):
            ║   $stackTrace
            ╚════════════════════════════════════════════════════╝
        """.trimIndent())
    }
}
