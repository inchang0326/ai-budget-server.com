package com.teady.aibudgetserver.adapter.secondary.jpa

import com.teady.aibudgetserver.adapter.secondary.jpa.port.BoardRepositoryPort
import com.teady.aibudgetserver.domain.board.entity.Board
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