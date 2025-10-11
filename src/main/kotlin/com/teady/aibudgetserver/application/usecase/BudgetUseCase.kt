package com.teady.aibudgetserver.application.usecase

import com.teady.aibudgetserver.adapter.primary.web.port.WebBudgetAdapterPort
import com.teady.aibudgetserver.adapter.secondary.jpa.budget.port.BudgetRepositoryPort
import com.teady.aibudgetserver.application.dto.TransactionDto
import com.teady.aibudgetserver.domain.budget.executor.TransactionExecutor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
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
        transactionExecutor.preExecute();

        val pageable: Pageable = PageRequest.of(
            page - 1,
            limit,
            Sort.by(Sort.Direction.DESC, "id.timestamp")
        )

        val sDate: LocalDate = YearMonth.of(year, month).atDay(1)
        val startTime: String = String.format("%04d%02d%02d000000000", sDate.year, sDate.monthValue, sDate.dayOfMonth)

        val eDate: LocalDate = sDate.plusMonths(1)
        val endTime: String = String.format("%04d%02d%02d000000000", eDate.year, eDate.monthValue, eDate.dayOfMonth)

        return budgetRepositoryPort.findAllByUserIdAndPeriodWithPaging(userId, startTime, endTime, pageable)
            .map { TransactionDto.fromEntity(it) }
    }
    override fun transactions(userId: String, year: Int, month: Int): List<TransactionDto> {
        transactionExecutor.preExecute();

        val sDate: LocalDate = YearMonth.of(year, month).atDay(1)
        val startTime: String = String.format("%04d%02d%02d000000000", sDate.year, sDate.monthValue, sDate.dayOfMonth)

        val eDate: LocalDate = sDate.plusMonths(1)
        val endTime: String = String.format("%04d%02d%02d000000000", eDate.year, eDate.monthValue, eDate.dayOfMonth)

        return budgetRepositoryPort.findAllByUserIdAndPeriod(userId, startTime, endTime)
            .map { TransactionDto.fromEntity(it) }
    }

    override fun transactionsCount(userId: String, year: Int, month: Int): Long {
        transactionExecutor.preExecute();

        val sDate: LocalDate = YearMonth.of(year, month).atDay(1)
        val startTime: String = String.format("%04d%02d%02d000000000", sDate.year, sDate.monthValue, sDate.dayOfMonth)

        val eDate: LocalDate = sDate.plusMonths(1)
        val endTime: String = String.format("%04d%02d%02d000000000", eDate.year, eDate.monthValue, eDate.dayOfMonth)

        return budgetRepositoryPort.findAllCountByUserIdAndPeriod(userId, startTime, endTime)
    }
}