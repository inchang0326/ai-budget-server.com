package com.teady.budgetserver.adapter.secondary.jpa.budget.port

import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistory
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistoryId
import com.teady.budgetserver.domain.budget.entity.TransactionId
import com.teady.budgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface BudgetRepositoryPort {
    fun selectAllTransactionsByUserIdWithPaging(userId: String, pageable: Pageable): Page<Transactions>
    fun selectAllTransactionsByUserIdAndPeriodWithPaging(
        userId: String,
        startTime: String,
        endTime: String,
        pageable: Pageable
    ): Page<Transactions>

    fun selectAllTransactionsByUserIdAndPeriod(userId: String, startTime: String, endTime: String): List<Transactions>
    fun selectAllTransactionsCountByUserIdAndPeriod(userId: String, startTime: String, endTime: String): Long
    fun selectTransactionById(transactionId: TransactionId): Optional<Transactions>
    fun insertTransaction(transactions: Transactions): Transactions
    fun deleteTransactionById(transactionsId: TransactionId)
    fun deleteAllTransactionsByUserId(userId: String)
    fun selectOpenBankingCardHistoryByUserIdAndPeriod(userId: String, startTime: String, endTime: String): List<OpenBankingCardHistory>
    fun selectOpenBankingCardCreatedTimeByUserIdAndCardNo(userId: String, cardNo: String): LocalDateTime?
    fun insertOpenBankingCardHisotry(openBankingCardHisotry: OpenBankingCardHistory): OpenBankingCardHistory
    fun deleteOpenBankingCardHistoryById(openBankingCardHistoryId: OpenBankingCardHistoryId)
    fun deleteAllOpenBankingCardHistoryByUserId(userId: String)
}