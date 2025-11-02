package com.teady.budgetserver.adapter.primary.web.port

import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithClient

interface WebBudgetOpenBankingAdapterPort {
    fun cards(userId: String): List<OpenBankingCardDtoWithClient>
    fun cards(userId: String, openBankingCardDtoWithClient: OpenBankingCardDtoWithClient)
    fun cardCreatedTime(userId: String, openBankingCardDtoWithClient: OpenBankingCardDtoWithClient): String?
}