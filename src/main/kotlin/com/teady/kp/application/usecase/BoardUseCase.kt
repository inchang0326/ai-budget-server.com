package com.teady.kp.application.usecase

import com.teady.kp.application.dto.BoardDto
import com.teady.kp.domain.board.entity.Board
import com.teady.kp.domain.board.executor.BoardExecutor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
@Slf4j
class BoardUseCase(
    private val boardExecutor: BoardExecutor
) {
    fun upload(boardDto : BoardDto) : HttpStatus {
        boardExecutor.upload(boardDto)
        return HttpStatus.OK
    }

    fun items() : List<BoardDto> {
        val list: MutableIterable<Board> = boardExecutor.items()
        return list.map { b -> BoardDto.fromEntity(b) }.toList()
    }
}