package com.teady.aibudgetserver.adapter.secondary.jpa.budget

import com.teady.aibudgetserver.adapter.secondary.jpa.budget.port.BudgetRepositoryPort
import com.teady.aibudgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class BudgetRepository(private val jpaTransactionRepository: JpaTransactionRepository) : BudgetRepositoryPort {
    override fun findAllByUserIdWithPaging(userId: String, pageable: Pageable): Page<Transactions> =
        jpaTransactionRepository.findAllByUserIdWithPaging(userId, pageable)

    override fun findAllByUserIdAndPeriodWithPaging(
        userId: String,
        startTime: String,
        endTime: String,
        pageable: Pageable
    ): Page<Transactions> =
        jpaTransactionRepository.findAllByUserIdAndPeriodWithPaging(userId, startTime, endTime, pageable)

    override fun findAllByUserIdAndPeriod(
        userId: String,
        startTime: String,
        endTime: String,
    ): List<Transactions> = jpaTransactionRepository.findAllByUserIdAndPeriod(userId, startTime, endTime)

    override fun findAllCountByUserIdAndPeriod(userId: String, startTime: String, endTime: String): Long =
        jpaTransactionRepository.findAllCountByUserIdAndPeriod(userId, startTime, endTime)

    override fun deleteByUserIdAndTimestamp(userId: String, timestamp: String) {
        jpaTransactionRepository.deleteByUserIdAndTimestamp(userId, timestamp)
    }
}