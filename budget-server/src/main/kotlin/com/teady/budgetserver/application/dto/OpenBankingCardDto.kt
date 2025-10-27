package com.teady.budgetserver.application.dto

data class OpenBankingCardDto(
    val no: String?,
    val noList: List<String>?,
    val name: String?,
    val company: String?,
    val syncAt: String?,
) {
}