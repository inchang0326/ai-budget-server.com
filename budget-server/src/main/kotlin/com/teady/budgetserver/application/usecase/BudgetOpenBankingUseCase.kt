package com.teady.budgetserver.application.usecase

import com.teady.budgetserver.adapter.primary.web.port.WebBudgetOpenBankingAdapterPort
import com.teady.budgetserver.adapter.secondary.jpa.budget.port.BudgetRepositoryPort
import com.teady.budgetserver.adapter.secondary.openapi.openbanking.OpenBankingAdapter
import com.teady.budgetserver.application.dto.OpenBankingCardDtoLikeHeaderWithExternalServer
import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithClient
import com.teady.budgetserver.application.dto.OpenBankingCardDtoWithExternalServer
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistory
import com.teady.budgetserver.domain.budget.entity.TransactionTypeEnum
import com.teady.budgetserver.domain.budget.executor.OpenBankingCardExecutor
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class BudgetOpenBankingUseCase(
    private val budgetOpenBankingCardExecutor: OpenBankingCardExecutor,
    private val budgetRepositoryPort: BudgetRepositoryPort,
    private val openBankingAdapter: OpenBankingAdapter
) : WebBudgetOpenBankingAdapterPort {
    override fun cards(userId: String): List<OpenBankingCardDtoWithClient> {
        budgetOpenBankingCardExecutor.preExecute()
        openBankingAdapter.getFinCardNumber("19920326", "9411123456782718")
        // 1. 프론트엔드에서 취득한 고객ID 기준 오픈뱅킹 카드 목록 조회
        // 2. 오픈뱅킹 카드 목록 그대로 서빙
        Thread.sleep(3000)
        return listOf(
            OpenBankingCardDtoWithClient("1234-****-****-5678", null, "신한카드", "신한카드", "2025-10-26"),
            OpenBankingCardDtoWithClient("9876-****-****-1234", null, "삼성카드", "삼성카드", "2025-10-25"),
            OpenBankingCardDtoWithClient("5555-****-****-6666", null, "국민카드", "국민카드", "2025-10-24"),
            OpenBankingCardDtoWithClient("7777-****-****-8888", null, "현대카드", "현대카드", "2025-10-23"),
            OpenBankingCardDtoWithClient("3333-****-****-4444", null, "우리카드", "우리카드", "2025-10-22"),
            OpenBankingCardDtoWithClient("2222-****-****-3333", null, "하나카드", "하나카드", "2025-10-21"),
            OpenBankingCardDtoWithClient("4444-****-****-5555", null, "롯데카드", "롯데카드", "2025-10-20"),
            OpenBankingCardDtoWithClient("6666-****-****-7777", null, "농협카드", "농협카드", "2025-10-19")
        )
    }

    override fun cardCreatedTime(userId: String, openBankingCardDtoWithClient: OpenBankingCardDtoWithClient): String? {
        openBankingCardDtoWithClient.no ?: return null
        val localDateTime = budgetRepositoryPort.selectOpenBankingCardCreatedTimeByUserIdAndCardNo(
            userId, openBankingCardDtoWithClient.no
        )
        return localDateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    override fun cards(userId: String, openBankingCardDtoWithClient: OpenBankingCardDtoWithClient) {
        openBankingCardDtoWithClient.noList ?: return
        budgetOpenBankingCardExecutor.preExecute()
        // 1. 프론트엔드에서 취득한 카드번호 기준 오픈뱅킹 카드 사용 승인 내역 조회
        // 2. 카드 사용 승인 내역 기준 Dto 전환 to Entity(OpenBankingCardHistory)
        // 3. OPEN_BANKING_CARD_HISTORY 테이블에 데이터 삽입
        budgetRepositoryPort.insertOpenBankingCardHisotry(
            OpenBankingCardHistory(
                userId = "00000000000000000001",
                cardNo = "1234********5678",
                timestamp = "20251009000000111",
                cardCompanyCode = "0001",
                type = TransactionTypeEnum.income,
                amount = 1000000.0,
                category = "부수입",
                description = "멘토링"
            )
        )
    }
}