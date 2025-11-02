package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.adapter.secondary.openapi.openbanking.port.OpenBankingFeignClientPort
import com.teady.budgetserver.application.dto.OpenBankingCardDtoLikeHeaderWithExternalServer
import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithExternalServer
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

@Component
class OpenBankingAdapter(
    private val openBankingFeignClient: OpenBankingFeignClient
) : OpenBankingFeignClientPort {
    override fun getFinCardNumber(brdt: String, cano: String): OpenBankingCardDtoWithExternalServer {
        val apiInfo = getApiInfo(openBankingFeignClient::getFinCardNumber)
        return openBankingFeignClient.getFinCardNumber(
            OpenBankingCardDtoWithExternalServer(
                OpenBankingCardDtoLikeHeaderWithExternalServer(apiInfo.apiNm, apiInfo.apiSvcCd),
                brdt,
                cano
            )
        )
    }

    private fun getApiInfo(method: KFunction<*>): OpenBankingApiInfo {
        val annotation = method.findAnnotation<OpenBankingApiInfo>()
            ?: throw IllegalArgumentException("@OpenBankingApiInfo not found on method: ${method.name}")

        return OpenBankingApiInfo(annotation.apiNm, annotation.apiSvcCd)
    }
}