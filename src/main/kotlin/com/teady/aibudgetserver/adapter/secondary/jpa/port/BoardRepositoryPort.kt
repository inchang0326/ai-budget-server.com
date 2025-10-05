package com.teady.aibudgetserver.adapter.secondary.jpa.port

import com.teady.aibudgetserver.domain.board.entity.Board

interface BoardRepositoryPort {
    fun save(board: Board)
    fun findAll() : MutableIterable<Board>
}