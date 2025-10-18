package com.teady.budgetserver.adapter.secondary.jpa.budget.port

import com.teady.budgetserver.domain.budget.entity.TransactionId
import com.teady.budgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface BudgetRepositoryPort {
    fun selectAllByUserIdWithPaging(userId: String, pageable: Pageable): Page<Transactions>
    fun selectAllByUserIdAndPeriodWithPaging(
        userId: String,
        startTime: String,
        endTime: String,
        pageable: Pageable
    ): Page<Transactions>

    fun selectAllByUserIdAndPeriod(userId: String, startTime: String, endTime: String): List<Transactions>
    fun selectAllCountByUserIdAndPeriod(userId: String, startTime: String, endTime: String): Long
    fun selectById(transactionId: TransactionId): Optional<Transactions>
    fun insertTransaction(transactions: Transactions): Transactions
    fun deleteByUserIdAndTimestamp(userId: String, timestamp: String)
    fun deleteAllByUserId(userId: String)
}