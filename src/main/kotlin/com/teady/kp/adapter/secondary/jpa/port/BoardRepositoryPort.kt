package com.teady.kp.adapter.secondary.jpa.port

import com.teady.kp.domain.board.entity.Board

interface BoardRepositoryPort {
    fun save(board: Board)
    fun findAll() : MutableIterable<Board>
}