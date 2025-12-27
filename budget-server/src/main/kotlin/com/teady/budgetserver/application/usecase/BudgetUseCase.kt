package com.teady.budgetserver.application.usecase

import com.teady.budgetserver.adapter.primary.web.port.WebBudgetAdapterPort
import com.teady.budgetserver.adapter.secondary.jpa.budget.port.BudgetRepositoryPort
import com.teady.budgetserver.application.dto.TransactionDto
import com.teady.budgetserver.application.dto.toCardNo
import com.teady.budgetserver.application.dto.toTimestamp
import com.teady.budgetserver.application.dto.toUserId
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistoryId
import com.teady.budgetserver.domain.budget.entity.TransactionId
import com.teady.budgetserver.domain.budget.executor.TransactionExecutor
import io.sentry.Sentry
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth

@Service
class BudgetUseCase(
    private val transactionExecutor: TransactionExecutor,
    private val budgetRepositoryPort: BudgetRepositoryPort
) : WebBudgetAdapterPort {

    override fun transactionsWithPaging(
        userId: String,
        year: Int,
        month: Int,
        page: Int,
        limit: Int
    ): Page<TransactionDto> {
        val pageable: Pageable = PageRequest.of(
            page - 1,
            limit,
            Sort.by(Sort.Direction.DESC, "id.timestamp")
        )

        return executeTransactions(
            year,
            month
        ) { startTime, endTime ->
            budgetRepositoryPort.selectAllTransactionsByUserIdAndPeriodWithPaging(
                userId,
                startTime,
                endTime,
                pageable
            )
        }.map { TransactionDto.fromEntity(it) }
    }

    override fun transactions(userId: String, year: Int, month: Int): List<TransactionDto> {

        try {
            throw Exception("This is a test.")
        } catch (e: Exception) {
            Sentry.captureException(e)
        }

        val list = ArrayList<TransactionDto>()

        list.addAll(executeTransactions(
            year,
            month
        ) { startTime, endTime ->
            budgetRepositoryPort.selectAllTransactionsByUserIdAndPeriod(
                userId,
                startTime,
                endTime
            )
        }.map { TransactionDto.fromEntity(it) })

        list.addAll(
            executeTransactions(
                year,
                month
            ) { startTime, endTime ->
                budgetRepositoryPort.selectOpenBankingCardHistoryByUserIdAndPeriod(
                    userId,
                    startTime,
                    endTime
                )
            }.map { TransactionDto.fromEntity(it) })

        return list.sortedByDescending { it.timestamp }
    }

    override fun transactionsCount(userId: String, year: Int, month: Int): Long {
        return executeTransactions(
            year,
            month
        ) { startTime, endTime ->
            budgetRepositoryPort.selectAllTransactionsCountByUserIdAndPeriod(
                userId,
                startTime,
                endTime
            )
        }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun transactions(userId: String, transactionDto: TransactionDto) {
        transactionExecutor.preExecute()
        budgetRepositoryPort.insertTransaction(transactionDto.toEntity(userId))
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun transactions(transactionDto: TransactionDto) {
        transactionExecutor.preExecute()
        transactionDto.id ?: return
        transactionDto.type ?: return
        transactionDto.amount ?: return
        transactionDto.category ?: return
        transactionDto.description ?: return
        val transactionId =
            TransactionId(userId = transactionDto.id.toUserId(), timestamp = transactionDto.id.toTimestamp())
        val transaction = budgetRepositoryPort.selectTransactionById(transactionId)
        transaction.ifPresent { t ->
            t.update(
                type = transactionDto.type,
                amount = transactionDto.amount,
                category = transactionDto.category,
                description = transactionDto.description
            )
        }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun transactionsDelete(transactionDto: TransactionDto) {
        transactionExecutor.preExecute()
        transactionDto.id ?: return

        if (transactionDto.cardNo == null) budgetRepositoryPort.deleteTransactionById(
            TransactionId(
                transactionDto.id.toUserId(),
                transactionDto.id.toTimestamp()
            )
        ) else budgetRepositoryPort.deleteOpenBankingCardHistoryById(
            OpenBankingCardHistoryId(
                transactionDto.id.toUserId(),
                transactionDto.id.toCardNo(),
                transactionDto.id.toTimestamp()
            )
        )

    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun transactionsDeleteAll(userId: String) {
        transactionExecutor.preExecute()
        budgetRepositoryPort.deleteAllTransactionsByUserId(userId)
        budgetRepositoryPort.deleteAllOpenBankingCardHistoryByUserId(userId)
    }

    private fun <T> executeTransactions(
        year: Int,
        month: Int,
        transactions: (startTime: String, endTime: String) -> T
    ): T {
        transactionExecutor.preExecute();

        val sDate: LocalDate = YearMonth.of(year, month).atDay(1)
        val startTime: String = String.format("%04d%02d%02d000000000", sDate.year, sDate.monthValue, sDate.dayOfMonth)

        val eDate: LocalDate = sDate.plusMonths(1)
        val endTime: String = String.format("%04d%02d%02d000000000", eDate.year, eDate.monthValue, eDate.dayOfMonth)

        return transactions(startTime, endTime)
    }
}