package com.teady.budgetserver.adapter.secondary.jpa.budget

import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistory
import com.teady.budgetserver.domain.budget.entity.OpenBankingCardHistoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface JpaOpenBankingCardRepository : JpaRepository<OpenBankingCardHistory, OpenBankingCardHistoryId> {
    @Query(
        """
        SELECT o FROM OpenBankingCardHistory o 
        WHERE o.id.userId = :userId 
        AND o.id.timestamp >= :startTime 
        AND o.id.timestamp < :endTime
        ORDER BY o.id.timestamp DESC
        """
    )
    fun selectAllByUserIdAndPeriod(
        @Param("userId") userId: String,
        @Param("startTime") startTime: String,
        @Param("endTime") endTime: String,
    ): List<OpenBankingCardHistory>

    @Query(
        "SELECT o.createdAt FROM OpenBankingCardHistory o WHERE o.id.userId = :userId AND o.id.cardNo = :cardNo LIMIT 1",
        nativeQuery = true
    )
    fun selectCreatedTimeByUserIdAndCardNo(
        @Param("userId") userId: String, @Param("cardNo") cardNo: String
    ): LocalDateTime?

    @Modifying
    @Query("DELETE FROM OpenBankingCardHistory o WHERE o.id.userId = :userId")
    fun deleteAllByUserId(
        @Param("userId") userId: String
    )
}