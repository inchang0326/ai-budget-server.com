package com.teady.aibudgetserver.adapter.primary.web.port

import com.teady.aibudgetserver.application.dto.TransactionDto
import com.teady.aibudgetserver.domain.budget.entity.Transactions
import org.springframework.data.domain.Page

interface WebBudgetAdapterPort {
    fun transactionsWithPaging(userId: String, year: Int, month: Int, page: Int, limit: Int): Page<TransactionDto>
    fun transactions(userId: String, year: Int, month: Int): List<TransactionDto>
    fun transactionsCount(userId: String, year: Int, month: Int): Long
    fun transactions(userId: String, transactionDto: TransactionDto)
    fun transactions(transactionDto: TransactionDto)
    fun transactionsDelete(transactionDto: TransactionDto)
    fun transactionsDeleteAll(userId: String)
}