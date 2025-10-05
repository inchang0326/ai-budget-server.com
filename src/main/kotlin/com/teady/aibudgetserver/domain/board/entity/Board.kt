package com.teady.aibudgetserver.domain.board.entity;

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
class Board (
    author: String,
    contents: String,
    emotion: String
    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val boardId : Long = 0
    @Column
    val author : String = author
    @Column
    val contents : String = contents
    @Column
    val emotion : String = emotion

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt : String = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt : String = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
}
