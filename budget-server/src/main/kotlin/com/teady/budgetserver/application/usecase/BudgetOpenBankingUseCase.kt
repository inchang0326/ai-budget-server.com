package com.teady.budgetserver.application.usecase

import com.teady.budgetserver.adapter.primary.web.port.WebBudgetOpenBankingAdapterPort
import com.teady.budgetserver.adapter.secondary.jpa.budget.port.BudgetRepositoryPort
import com.teady.budgetserver.application.dto.OpenBankingCardDto
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistory
import com.teady.budgetserver.domain.budget.entity.TransactionType
import com.teady.budgetserver.domain.budget.executor.OpenBankingCardExecutor
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class BudgetOpenBankingUseCase(
    private val budgetOpenBankingCardExecutor: OpenBankingCardExecutor,
    private val budgetRepositoryPort: BudgetRepositoryPort
) : WebBudgetOpenBankingAdapterPort {
    override fun cards(userId: String): List<OpenBankingCardDto> {
        budgetOpenBankingCardExecutor.preExecute()
        TODO("select remote open-banking cards")
        // 1. 프론트엔드에서 취득한 고객ID 기준 오픈뱅킹 카드 목록 조회
        // 2. 오픈뱅킹 카드 목록 그대로 서빙
        return listOf(OpenBankingCardDto(""))
    }

    override fun cardCreatedTime(userId: String, openBankingCardDto: OpenBankingCardDto): String? {
        val localDateTime = budgetRepositoryPort.selectOpenBankingCardCreatedTimeByUserIdAndCardNo(userId, openBankingCardDto.cardNo)
        return localDateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    override fun cards(userId: String, openBankingCardDto: OpenBankingCardDto) {
        budgetOpenBankingCardExecutor.preExecute()
        TODO("synchronization remote open-banking card history to budget-server")
        // 1. 프론트엔드에서 취득한 카드번호 기준 오픈뱅킹 카드 사용 승인 내역 조회
        // 2. 카드 사용 승인 내역 기준 Dto 전환 to Entity(OpenBankingCardHistory)
        // 3. OPEN_BANKING_CARD_HISTORY 테이블에 데이터 삽입
        budgetRepositoryPort.insertOpenBankingCardHisotry(
            OpenBankingCardHistory(
                userId = "",
                cardNo = "",
                timestamp = "",
                cardCompanyCode = "",
                type = TransactionType.none,
                amount = 0.0,
                category = "",
                description = ""
            )
        )
    }
}