package com.teady.budgetserver.application.dto

import com.teady.budgetserver.domain.budget.entity.CardCompanyEnum
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistory
import com.teady.budgetserver.domain.budget.entity.Transactions
import com.teady.budgetserver.domain.budget.entity.TransactionTypeEnum
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TransactionDto(
    val id: String?,
    val type: TransactionTypeEnum?,
    val amount: Double?,
    val category: String?,
    val description: String?,
    val date: String?,
    val cardCompany: String?,
    val timestamp: String?,
    val cardNo: String?,
) {
    fun toEntity(userId: String): Transactions {

        val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val localDateTime = localDate.atTime(LocalTime.now())

        return Transactions(
            userId = userId,
            timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),
            type = type ?: TransactionTypeEnum.none,
            amount = amount ?: 0.0,
            category = category ?: "",
            description = description ?: ""
        )
    }

    companion object {
        fun fromEntity(t: Transactions): TransactionDto = TransactionDto(
            id = toId(t.id.userId, t.id.timestamp, null),
            type = t.type,
            amount = t.amount,
            category = t.category,
            description = t.description,
            date = LocalDateTime.parse(t.id.timestamp, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            cardCompany = null,
            timestamp = t.id.timestamp,
            cardNo = null,
        )

        fun fromEntity(o: OpenBankingCardHistory): TransactionDto = TransactionDto(
            id = toId(o.id.userId, o.id.timestamp, o.id.cardNo),
            type = o.type,
            amount = o.amount,
            category = o.category,
            description = o.description,
            date = LocalDateTime.parse(o.id.timestamp, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            cardCompany = CardCompanyEnum.getKoreanNameByCode(o.cardCompanyCode),
            timestamp = o.id.timestamp,
            cardNo = o.id.cardNo,
        )

    }
}

fun String.toUserId() = this.take(20)

fun String.toTimestamp(): String {
    val dropped = this.drop(20)
    return dropped.take(17)
}

fun String.toCardNo(): String {
    val dropped = this.drop(37)
    return dropped.take(16)
}

fun toId(userId: String, timestamp: String, cardNo: String?): String {
    if (cardNo == null) return userId.take(20).padStart(20, '0') + timestamp.take(17).padStart(17, '0')
    else return userId.take(20).padStart(20, '0') + timestamp.take(17).padStart(17, '0') + cardNo.take(16).padStart(16, '0')
}