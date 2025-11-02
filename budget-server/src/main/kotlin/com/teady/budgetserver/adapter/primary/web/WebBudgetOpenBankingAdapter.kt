package com.teady.budgetserver.adapter.primary.web

import PaginatedResponse
import com.teady.budgetserver.adapter.primary.web.port.WebBudgetOpenBankingAdapterPort
import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toSuccessResponse

@RestController
@RequestMapping("/budget/open-banking")
class WebBudgetOpenBankingAdapter(val webBudgetOpenBankingAdapterPort: WebBudgetOpenBankingAdapterPort) {
    @GetMapping("/cards")
    fun cards(
        @RequestHeader("X-USER-ID") userId: String
    ) = PaginatedResponse.of(
        items = webBudgetOpenBankingAdapterPort.cards(userId),
        page = 1,
        limit = 10,
        totalCount = 0
    ).toSuccessResponse()

    @GetMapping("/cards/created-time")
    fun cardCreatedTime(
        @RequestHeader("X-USER-ID") userId: String, @RequestBody openBankingCardDtoWithClient: OpenBankingCardDtoWithClient
    ) = webBudgetOpenBankingAdapterPort.cardCreatedTime(userId, openBankingCardDtoWithClient).toSuccessResponse()

    @PostMapping("/cards")
    fun cards(
        @RequestHeader("X-USER-ID") userId: String, @RequestBody openBankingCardDtoWithClient: OpenBankingCardDtoWithClient
    ) = webBudgetOpenBankingAdapterPort.cards(userId, openBankingCardDtoWithClient).toSuccessResponse()
}