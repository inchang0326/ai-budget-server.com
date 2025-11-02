package com.teady.budgetserver.adapter.secondary.openapi.openbanking.port

import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithExternalServer
import org.springframework.web.bind.annotation.RequestBody

interface OpenBankingFeignClientPort {
    fun getFinCardNumber(brdt: String, cano: String): OpenBankingCardDtoWithExternalServer
}