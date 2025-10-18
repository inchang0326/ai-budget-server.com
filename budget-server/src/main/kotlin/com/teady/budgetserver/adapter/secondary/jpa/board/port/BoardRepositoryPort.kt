package com.teady.budgetserver.adapter.secondary.jpa.board.port

import com.teady.budgetserver.domain.board.entity.Board

interface BoardRepositoryPort {
    fun save(board: Board)
    fun findAll() : MutableIterable<Board>
}