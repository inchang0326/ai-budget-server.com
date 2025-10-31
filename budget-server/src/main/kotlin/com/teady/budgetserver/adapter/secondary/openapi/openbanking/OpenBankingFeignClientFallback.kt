package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.application.dto.OpenBankingCardDto
import com.teady.budgetserver.global.exception.*
import feign.FeignException
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.stereotype.Component

/**
 * OpenBankingFeignClient에 대한 Fallback Factory
 * - 타겟 서버로부터 HTTP 오류 응답을 받지 못 했을 때 작동하는 Fallback => Exceptionhandler에 의해 처리
 * - 만약 HTTP 오류 응답을 받았다면 ErrorDecoder => Exceptionhandler에 의해 처리
 * 기준
 * - 읽기 작업: 기본값/빈 값 반환 (서비스 연속성)
 * - 쓰기 작업: 명시적 실패 (데이터 안전)
 */ @Component
class OpenBankingFallbackFactory : FallbackFactory<OpenBankingFeignClient> {

    override fun create(cause: Throwable) = object : OpenBankingFeignClient { // OpenBankingFeignClient를 상속한 익명 객체 object

        private fun guardAuthorization() {
            if (cause is FeignException.Unauthorized || cause is FeignException.Forbidden) {
                throw cause as? FeignClientException
                    ?: FeignUnauthorizedException()
            }
        }

        override fun getFinCardNumber(): String {
            guardAuthorization()
            return "FALLBACK_CARD_NUMBER"
        }

        override fun getCardHistory(): List<OpenBankingCardDto> {
            guardAuthorization()
            return emptyList()
        }

        override fun createFinCardNumber(): String {
            throw FeignServiceUnavailableException(
                message = "카드 생성 서비스를 일시적으로 사용할 수 없습니다"
            )
        }
    }
}
