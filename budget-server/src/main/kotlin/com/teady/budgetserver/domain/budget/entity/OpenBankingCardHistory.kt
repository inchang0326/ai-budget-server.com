package com.teady.budgetserver.domain.budget.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@Table(name = "open_banking_card_history")
@Entity
@EntityListeners(AuditingEntityListener::class)
class OpenBankingCardHistory(
    userId: String,
    cardNo: String,
    timestamp: String,
    cardCompanyCode: String,
    type: TransactionType,
    amount: Double,
    category: String,
    description: String
) {
    @EmbeddedId
    var id: OpenBankingCardHistoryId = OpenBankingCardHistoryId(
        userId = userId,
        cardNo = cardNo,
        timestamp = timestamp
    )
        protected set

    @Column(name = "card_company_code", length = 3, nullable = false)
    var cardCompanyCode: String = cardCompanyCode
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10, nullable = false)
    var type: TransactionType = type
        protected set

    @Column(name = "amount", nullable = false)
    var amount: Double = amount
        protected set

    @Column(name = "category", length = 100, nullable = false)
    var category: String = category
        protected set

    @Column(name = "description", length = 200, nullable = false)
    var description: String = description
        protected set

    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        protected set
}

@Embeddable
data class OpenBankingCardHistoryId(
    /**
     *  PK: (userId, cardNo)
     *  userId를 선두 컬럼으로 지정한 명확한 이유
     *  - create는 고려 대상이 아님
     *  - retrieve & delete 시 조회 파라미터는 실질적으로 userId 기준으로 이루어짐
     *  - update 시 cardNo 기준이나, userId 기준 모든 데이터 삭제 후 다시 create 하면 됨
     *
     *  PK: (userId, cardNo, timestamp)
     *  카드 내역으로 테이블을 정규화하면서, uniqueness를 보장하기 위해 timestamp를 추가함
     *
     *  PK: (userId, cardCompanyCode, timestamp)
     *  cardNo 데이터는 휘발성으로 프론트엔드-백엔드-오픈뱅킹 간 주고 받을 수 있는 값으로,
     *  실질적으로 데이터를 저장할 필요가 없음 추가로 개인 민감정보 따른 대응도 하지 않아도 됨
     *
     *  PK: (userId, cardNo, timestamp)
     *  메인 기능 가능성으로 인한 중요도, 카드사별 N개 카드 존재 가능성으로 Pk 복귀
     */
    @Column(name = "user_id", length = 20, nullable = false)
    val userId: String = "",

    @Column(name = "card_no", length = 16, nullable = false)
    val cardNo: String = "",

    @Column(name = "timestamp", length = 15, nullable = false)
    val timestamp: String = ""
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}