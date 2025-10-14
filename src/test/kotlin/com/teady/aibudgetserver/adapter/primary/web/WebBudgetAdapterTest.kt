package com.teady.aibudgetserver.adapter.primary.web

import com.teady.aibudgetserver.application.dto.TransactionDto
import com.teady.aibudgetserver.application.usecase.BudgetUseCase
import com.teady.aibudgetserver.domain.budget.entity.TransactionType
import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

import org.mockito.BDDMockito.given
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*

@WebMvcTest(WebBudgetAdapter::class)
@AutoConfigureRestDocs
class WebBudgetAdapterTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var budgetUseCase: BudgetUseCase

    @MockBean
    private lateinit var meterRegistry: MeterRegistry

    @MockBean
    private lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @Test
    fun transactions() {
        val userId: String = "1"
        val year: Int = 2025
        val month: Int = 10

        val transactions: List<TransactionDto> = listOf(
            (TransactionDto(
                "120251009000000111",
                TransactionType.income,
                1000000.0,
                "부수익",
                "앱테크",
                "2025-10-24",
                "1"
            ))
        )

        given(budgetUseCase.transactions(userId, year, month)).willReturn(transactions)

        mockMvc
            .perform(
                get("/budget/transactions")
                    .param("userId", userId)
                    .param("year", year.toString())
                    .param("month", month.toString())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "transactions",
                    queryParameters(
                        parameterWithName("userId")
                            .description("사용자 ID"),
                        parameterWithName("year")
                            .description("해당 년도")
                            .optional(),
                        parameterWithName("month")
                            .description("해당 월")
                            .optional(),
                    ),
                    relaxedResponseFields(
                        fieldWithPath("data.items[].id").description("사용자 ID + timestamp"),
                        fieldWithPath("data.items[].type").description("거래 유형 (수입 또는 지출)"),
                        fieldWithPath("data.items[].amount").description("거래 금액"),
                        fieldWithPath("data.items[].category").description("거래 범주"),
                        fieldWithPath("data.items[].description").description("상세 설명"),
                        fieldWithPath("data.items[].date").description("거래 일자"),
                        fieldWithPath("data.items[].userId").description("사용자 ID")
                    )
                )
            )

    }
}