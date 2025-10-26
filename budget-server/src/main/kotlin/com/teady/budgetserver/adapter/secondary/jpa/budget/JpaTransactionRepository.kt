package com.teady.budgetserver.adapter.secondary.jpa.budget

import com.teady.budgetserver.domain.budget.entity.Transactions
import com.teady.budgetserver.domain.budget.entity.TransactionId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaTransactionRepository : JpaRepository<Transactions, TransactionId> {

    @Query("SELECT t FROM Transactions t WHERE t.id.userId = :userId")
    fun selectAllByUserIdWithPaging(@Param("userId") userId: String, pageable: Pageable): Page<Transactions>

    @Query(
        """
        SELECT t FROM Transactions t 
        WHERE t.id.userId = :userId 
        AND t.id.timestamp >= :startTime 
        AND t.id.timestamp < :endTime
        """
    )
    fun selectAllByUserIdAndPeriodWithPaging(
        @Param("userId") userId: String,
        @Param("startTime") startTime: String,
        @Param("endTime") endTime: String,
        pageable: Pageable
    ): Page<Transactions>

    @Query(
        """
        SELECT t FROM Transactions t 
        WHERE t.id.userId = :userId 
        AND t.id.timestamp >= :startTime 
        AND t.id.timestamp < :endTime
        ORDER BY t.id.timestamp DESC
        """
    )
    fun selectAllByUserIdAndPeriod(
        @Param("userId") userId: String,
        @Param("startTime") startTime: String,
        @Param("endTime") endTime: String,
    ): List<Transactions>

    @Query(
        """
        SELECT count(t) FROM Transactions t 
        WHERE t.id.userId = :userId 
        AND t.id.timestamp >= :startTime 
        AND t.id.timestamp < :endTime
        """
    )
    fun selectAllCountByUserIdAndPeriod(
        @Param("userId") userId: String,
        @Param("startTime") startTime: String,
        @Param("endTime") endTime: String,
    ): Long

    @Modifying
    @Query("DELETE FROM Transactions t WHERE t.id.userId = :userId")
    fun deleteAllByUserId(
        @Param("userId") userId: String
    )
}