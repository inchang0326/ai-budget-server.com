package com.teady.aibudgetserver.adapter.secondary.jpa.budget.port

import com.teady.aibudgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BudgetRepositoryPort {
    fun findAllByUserIdWithPaging(userId: String, pageable: Pageable): Page<Transactions>
    fun findAllByUserIdAndPeriodWithPaging(userId: String, startTime: String, endTime: String, pageable: Pageable): Page<Transactions>
    fun findAllByUserIdAndPeriod(userId: String, startTime: String, endTime: String): List<Transactions>
    fun findAllCountByUserIdAndPeriod(userId: String, startTime: String, endTime: String): Long
    fun deleteByUserIdAndTimestamp(userId: String, timestamp: String)
}