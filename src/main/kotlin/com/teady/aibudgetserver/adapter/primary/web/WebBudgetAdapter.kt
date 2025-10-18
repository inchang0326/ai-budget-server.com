package com.teady.aibudgetserver.adapter.primary.web

import PaginatedResponse
import com.teady.aibudgetserver.adapter.primary.web.port.WebBudgetAdapterPort
import com.teady.aibudgetserver.application.dto.TransactionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toSuccessResponse
import java.time.LocalDate

@RestController
@RequestMapping("/budget")
class WebBudgetAdapter(
    private val webBudgetAdapterPort: WebBudgetAdapterPort,
) {
    @GetMapping("/transactions")
    fun transactions(
        @RequestHeader("X-USER-ID") userId: String,
        @RequestParam(required = false) year: Int,
        @RequestParam(required = false) month: Int,
    ) = PaginatedResponse.of(
        items = webBudgetAdapterPort.transactions(
            userId,
            year ?: LocalDate.now().year,
            month ?: LocalDate.now().monthValue,
        ),
        page = 1,
        limit = 10,
        totalCount = 0
    ).toSuccessResponse()

    @PostMapping("/transactions")
    fun transactions(
        @RequestHeader("X-USER-ID") userId: String,
        @RequestBody transactionDto: TransactionDto
    ) = webBudgetAdapterPort.transactions(transactionDto.toEntity(userId))

    @PostMapping("/transactions/delete")
    fun transactionsDelete(@RequestBody transactionDto: TransactionDto) =
        webBudgetAdapterPort.transactionsDelete(transactionDto).toSuccessResponse()

    @PostMapping("/transactions/delete-all")
    fun transactionsDeleteAll(@RequestHeader("X-USER-ID") userId: String) =
        webBudgetAdapterPort.transactionsDeleteAll(userId).toSuccessResponse()
}