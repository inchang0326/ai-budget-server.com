package com.teady.budgetserver.adapter.secondary.jpa.board

import com.teady.budgetserver.adapter.secondary.jpa.board.port.BoardRepositoryPort
import com.teady.budgetserver.domain.board.entity.Board
import org.springframework.stereotype.Repository

@Repository
class BoardRepository(
    private val jpaBoardRepository: JpaBoardRepository
) : BoardRepositoryPort {
    override fun save(board: Board) {
        jpaBoardRepository.save(board)
    }
    override fun findAll(): MutableIterable<Board> {
        return jpaBoardRepository.findAll()
    }
}