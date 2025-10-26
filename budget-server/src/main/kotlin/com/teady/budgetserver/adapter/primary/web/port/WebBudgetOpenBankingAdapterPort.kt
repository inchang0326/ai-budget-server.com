package com.teady.budgetserver.adapter.primary.web.port

import com.teady.budgetserver.application.dto.OpenBankingCardDto
import java.time.LocalDateTime

interface WebBudgetOpenBankingAdapterPort {
    fun cards(userId: String): List<OpenBankingCardDto>
    fun cards(userId: String, openBankingCardDto: OpenBankingCardDto)
    fun cardCreatedTime(userId: String, openBankingCardDto: OpenBankingCardDto): String?
}