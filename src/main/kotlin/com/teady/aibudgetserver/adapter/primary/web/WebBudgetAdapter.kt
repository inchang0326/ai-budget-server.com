package com.teady.aibudgetserver.adapter.primary.web

import PaginatedResponse
import com.teady.aibudgetserver.adapter.primary.web.port.WebBudgetAdapterPort
import org.springframework.web.bind.annotation.GetMapping
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
        @RequestParam userId: String,
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
}