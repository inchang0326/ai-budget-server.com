package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.global.exception.*
import feign.Logger
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenBankingFeignConfig {

    /**
     * Feign 로깅 레벨 설정
     * - NONE: 로깅 없음
     * - BASIC: 요청 메서드, URL, 응답 상태 코드, 실행 시간만 로깅
     * - HEADERS: BASIC + 요청/응답 헤더
     * - FULL: HEADERS + 요청/응답 본문
     */
    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    /**
     * Request Interceptor
     * 모든 요청에 공통 헤더나 로직을 추가
     */
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            // 공통 헤더 추가
            println("Feign Request: ${template.method()} ${template.url()} ${String(template.body())}")
        }
    }

    /**
     * Error Decoder
     * HTTP 에러 응답을 커스텀 예외로 변환
     */
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return ErrorDecoder { methodKey, response ->
            when (response.status()) {
                400 -> FeignBadRequestException()
                401 -> FeignUnauthorizedException()
                403 -> FeignForbiddenException()
                404 -> FeignResourceNotFoundException()
                500 -> FeignInternalServerException()
                503 -> FeignServiceUnavailableException()
                else -> ErrorDecoder.Default().decode(methodKey, response)
            }
        }
    }
}