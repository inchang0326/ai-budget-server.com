package com.teady.aibudgetserver.adapter.secondary.jpa.budget

import com.teady.aibudgetserver.domain.budget.entity.Transactions
import com.teady.aibudgetserver.domain.budget.entity.TransactionId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

interface JpaTransactionRepository : PagingAndSortingRepository<Transactions, TransactionId> {

    @Query("SELECT t FROM Transactions t WHERE t.id.userId = :userId")
    fun findAllByUserIdWithPaging(@Param("userId") userId: String, pageable: Pageable): Page<Transactions>

    @Query(
        """
        SELECT t FROM Transactions t 
        WHERE t.id.userId = :userId 
        AND t.id.timestamp >= :startTime 
        AND t.id.timestamp < :endTime
        """
    )
    fun findAllByUserIdAndPeriodWithPaging(
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
    fun findAllByUserIdAndPeriod(
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
    fun findAllCountByUserIdAndPeriod(
        @Param("userId") userId: String,
        @Param("startTime") startTime: String,
        @Param("endTime") endTime: String,
    ): Long
}