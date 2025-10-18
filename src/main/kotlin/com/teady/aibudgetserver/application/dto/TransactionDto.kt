package com.teady.aibudgetserver.application.dto

import com.teady.aibudgetserver.domain.budget.entity.Transactions
import com.teady.aibudgetserver.domain.budget.entity.TransactionType
import com.teady.aibudgetserver.global.util.toId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TransactionDto(
    val id: String?,
    val type: TransactionType?,
    val amount: Double?,
    val category: String?,
    val description: String?,
    val date: String?,
    val userId: String?,
) {
    fun toEntity(userId: String): Transactions {

        val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val localDateTime = localDate.atTime(LocalTime.now())

        return Transactions(
            userId = userId,
            timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),
            type = type ?: TransactionType.none,
            amount = amount ?: 0.0,
            category = category ?: "",
            description = description ?: ""
        )
    }

    companion object {
        fun fromEntity(t: Transactions): TransactionDto = TransactionDto(
            id = toId(t.id.userId, t.id.timestamp),
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