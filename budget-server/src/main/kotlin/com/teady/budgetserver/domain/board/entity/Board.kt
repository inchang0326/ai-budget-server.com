package com.teady.budgetserver.domain.board.entity;

import jakarta.persistence.Column
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id;
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Table(name = "board")
@Entity
@EntityListeners(AuditingEntityListener::class)
class Board(
    author: String,
    contents: String,
    emotion: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardId: Long = 0
        protected set

    @Column
    var author: String = author
        protected set

    @Column
    var contents: String = contents
        protected set

    @Column
    var emotion: String = emotion
        protected set

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: String = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
        protected set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: String = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
        protected set
}
