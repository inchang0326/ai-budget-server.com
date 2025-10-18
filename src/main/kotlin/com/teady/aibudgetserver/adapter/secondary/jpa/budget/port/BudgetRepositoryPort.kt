package com.teady.aibudgetserver.adapter.secondary.jpa.budget.port

import com.teady.aibudgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

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
    fun save(transactions: Transactions): Transactions
    fun deleteByUserIdAndTimestamp(userId: String, timestamp: String)
    fun deleteAllByUserId(userId: String)
}