package com.teady.aibudgetserver.application.dto

import com.teady.aibudgetserver.domain.budget.entity.Transactions
import com.teady.aibudgetserver.domain.budget.entity.TransactionType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TransactionDto(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val description: String,
    val date: String,
    val userId: String,
) {
    fun toEntity(): Transactions =
        Transactions(
            userId = userId,
            timestamp = date,
            type = type,
            amount = amount,
            category = category,
            description = description
        )

    companion object {
        fun fromEntity(t: Transactions): TransactionDto = TransactionDto(
            id = t.id.userId + t.id.timestamp,
            type = t.type,
            amount = t.amount,
            category = t.category,
            description = t.description,
            date = LocalDateTime.parse(t.id.timestamp, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            userId = t.id.userId,
        )
    }
}