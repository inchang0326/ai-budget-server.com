package com.teady.budgetserver.application.usecase

import com.teady.budgetserver.application.dto.BoardDto
import com.teady.budgetserver.adapter.primary.web.port.WebBoardAdapterPort
import com.teady.budgetserver.adapter.secondary.jpa.board.port.BoardRepositoryPort
//import com.teady.budgetserver.adapter.secondary.kafka.port.KafkaClientPort
import com.teady.budgetserver.domain.board.entity.Board
import com.teady.budgetserver.domain.board.executor.BoardExecutor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
@Slf4j
class BoardUseCase (
    private val boardExecutor: BoardExecutor,
    private val boardRepositoryPort: BoardRepositoryPort,
//    private val kafkaClientPort: KafkaClientPort
) : WebBoardAdapterPort {
    override fun upload(boardDto : BoardDto) : HttpStatus {
        val dto = boardExecutor.doSomething(boardDto)
        boardRepositoryPort.save(dto.toEntity())
//        kafkaClientPort.send("1", boardDto)
        return HttpStatus.OK
    }

    override fun items() : MutableIterable<BoardDto> {
        val boardDtolist: MutableIterable<Board> = boardRepositoryPort.findAll()
        return boardExecutor.doSomething(boardDtolist.map { b -> BoardDto.fromEntity(b) }.toMutableList())
    }
}