package com.teady.budgetserver.adapter.secondary.jpa.budget

import com.teady.budgetserver.adapter.secondary.jpa.budget.port.BudgetRepositoryPort
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistory
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistoryId
import com.teady.budgetserver.domain.budget.entity.TransactionId
import com.teady.budgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class BudgetRepository(
    private val jpaTransactionRepository: JpaTransactionRepository,
    private val jpaOpenBankingCardRepository: JpaOpenBankingCardRepository
) : BudgetRepositoryPort {
    override fun selectAllTransactionsByUserIdWithPaging(userId: String, pageable: Pageable): Page<Transactions> =
        jpaTransactionRepository.selectAllByUserIdWithPaging(userId, pageable)

    override fun selectAllTransactionsByUserIdAndPeriodWithPaging(
        userId: String, startTime: String, endTime: String, pageable: Pageable
    ): Page<Transactions> =
        jpaTransactionRepository.selectAllByUserIdAndPeriodWithPaging(userId, startTime, endTime, pageable)

    override fun selectAllTransactionsByUserIdAndPeriod(
        userId: String,
        startTime: String,
        endTime: String,
    ): List<Transactions> = jpaTransactionRepository.selectAllByUserIdAndPeriod(userId, startTime, endTime)

    override fun selectAllTransactionsCountByUserIdAndPeriod(userId: String, startTime: String, endTime: String): Long =
        jpaTransactionRepository.selectAllCountByUserIdAndPeriod(userId, startTime, endTime)

    override fun selectTransactionById(transactionId: TransactionId): Optional<Transactions> =
        jpaTransactionRepository.findById(transactionId)

    override fun insertTransaction(transactions: Transactions) = jpaTransactionRepository.save(transactions)

    override fun deleteTransactionById(transactionId: TransactionId) =
        jpaTransactionRepository.deleteById(transactionId)

    override fun deleteAllTransactionsByUserId(userId: String) = jpaTransactionRepository.deleteAllByUserId(userId)

    override fun selectOpenBankingCardHistoryByUserIdAndPeriod(
        userId: String,
        startTime: String,
        endTime: String
    ): List<OpenBankingCardHistory> = jpaOpenBankingCardRepository.selectAllByUserIdAndPeriod(userId, startTime, endTime)
    override fun selectOpenBankingCardCreatedTimeByUserIdAndCardNo(userId: String, cardNo: String) =
        jpaOpenBankingCardRepository.selectCreatedTimeByUserIdAndCardNo(userId, cardNo)
    override fun insertOpenBankingCardHisotry(openBankingCardHisotry: OpenBankingCardHistory): OpenBankingCardHistory =
        jpaOpenBankingCardRepository.save(openBankingCardHisotry)

    override fun deleteOpenBankingCardHistoryById(openBankingCardHistoryId: OpenBankingCardHistoryId) =
        jpaOpenBankingCardRepository.deleteById(openBankingCardHistoryId)

    override fun deleteAllOpenBankingCardHistoryByUserId(userId: String) =
        jpaOpenBankingCardRepository.deleteAllByUserId(userId)
}