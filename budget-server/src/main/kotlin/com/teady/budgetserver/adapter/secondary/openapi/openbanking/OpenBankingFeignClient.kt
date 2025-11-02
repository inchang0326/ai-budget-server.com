package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithExternalServer
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(
    name = "openbanking-api-client",
    url = "\${openbanking.api.base-url}",
    configuration = [OpenBankingFeignConfig::class],
    fallbackFactory = OpenBankingFeignClientFallbackFactory::class
)
interface OpenBankingFeignClient {
    @PostMapping("/OpenFinCardDirect.nh")
    @OpenBankingApiInfo("OpenFinCardDirect", "DrawingTransferA")
    fun getFinCardNumber(
        @RequestBody openBankingCardDtoWithExternalServer: OpenBankingCardDtoWithExternalServer
    ): OpenBankingCardDtoWithExternalServer

    @PostMapping("/OpenFinCardDirect.nh")
    fun createFinCardNumber(): String

    @PostMapping("/todo")
    fun getCardHistory(): List<OpenBankingCardDtoWithExternalServer>
}