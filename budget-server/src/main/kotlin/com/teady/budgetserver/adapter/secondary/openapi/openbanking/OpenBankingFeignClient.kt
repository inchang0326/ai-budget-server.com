package com.teady.budgetserver.adapter.secondary.openapi.openbanking

import com.teady.budgetserver.application.dto.OpenBankingCardDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(
    name = "openbanking-api-client",
    url = "\${openbanking.api.base-url}",
    configuration = [OpenBankingFeignConfig::class],
    fallback = OpenBankingFeignClientFallbackFactory::class
)
interface OpenBankingFeignClient {
    @GetMapping("/todo")
    fun getFinCardNumber(): String

    @PostMapping("/todo")
    fun createFinCardNumber(): String

    @PostMapping("/todo")
    fun getCardHistory(): List<OpenBankingCardDto>
}