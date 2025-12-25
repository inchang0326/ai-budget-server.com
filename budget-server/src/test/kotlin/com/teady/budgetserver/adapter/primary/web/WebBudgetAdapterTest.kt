package com.teady.budgetserver.adapter.primary.web

import com.teady.budgetserver.application.dto.TransactionDto
import com.teady.budgetserver.application.usecase.BudgetUseCase
import com.teady.budgetserver.domain.budget.entity.TransactionTypeEnum
import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders

@WebMvcTest(WebBudgetAdapter::class)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8000)
class WebBudgetAdapterTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var budgetUseCase: BudgetUseCase

    @MockBean
    private lateinit var meterRegistry: MeterRegistry

    @MockBean
    private lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @Value("\${gateway.secret}")
    private lateinit var gatewaySecret: String

    @Test
    fun transactions() {
        val userId: String = "00000000000000000001"
        val year: Int = 2025
        val month: Int = 10

        val transactions: List<TransactionDto> = listOf(
            (TransactionDto(
                "0000000000000000000120251009000000111",
                TransactionTypeEnum.income,
                1000000.0,
                "부수익",
                "앱테크",
                "2025-10-24",
                cardCompany = null,
                timestamp = "20251009000000111",
                cardNo = null,
            ))
        )

        given(budgetUseCase.transactions(userId, year, month)).willReturn(transactions)

        mockMvc
            .perform(
                get("/budget/transactions")
                    .header("X-GATEWAY-SECRET", gatewaySecret)
                    .header("X-USER-ID", userId)
                    .param("year", year.toString())
                    .param("month", month.toString())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(
                document(
                    "transactions",
                    requestHeaders(
                        headerWithName("X-USER-ID").description("사용자 ID")
                    ),
                    queryParameters(
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
                        fieldWithPath("data.items[].cardCompany").description("카드사"),
                        fieldWithPath("data.items[].timestamp").description("timestamp"),
                        fieldWithPath("data.items[].cardNo").description("카드번호")
                    )
                )
            )

    }
}