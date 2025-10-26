package com.teady.budgetserver.domain.budget.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@Table(name = "transactions")
@Entity
@EntityListeners(AuditingEntityListener::class)
class Transactions(
    userId: String,
    timestamp: String,
    type: TransactionType,
    amount: Double,
    category: String,
    description: String
) {
    @EmbeddedId
    var id: TransactionId = TransactionId(
        userId = userId,
        timestamp = timestamp
    )
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name="type", length = 10, nullable = false)
    var type: TransactionType = type
        protected set

    @Column(name="amount", nullable = false)
    var amount: Double = amount
        protected set

    @Column(name="category", length = 100, nullable = false)
    var category: String = category
        protected set

    @Column(name="description", length = 200, nullable = false)
    var description: String = description
        protected set

    @CreatedDate
    @Column(name="created_time", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        protected set

    @LastModifiedDate
    @Column(name="updated_time", nullable = false)
    var updatedAt: LocalDateTime? = null
        protected set

    fun update(type: TransactionType, amount: Double, category: String, description: String) {
        this.type = type
        this.amount = amount
        this.category = category
        this.description = description
    }
}

@Embeddable
data class TransactionId(
    @Column(name = "user_id", length = 20, nullable = false)
    val userId: String = "",

    @Column(name = "timestamp", length = 17, nullable = false)
    val timestamp: String = ""
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}

enum class TransactionType {
    income,
    expense,
    none,
}