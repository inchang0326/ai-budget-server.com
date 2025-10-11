package com.teady.aibudgetserver.application.usecase

import com.teady.aibudgetserver.application.dto.BoardDto
import com.teady.aibudgetserver.adapter.primary.web.port.WebBoardAdapterPort
import com.teady.aibudgetserver.adapter.secondary.jpa.board.port.BoardRepositoryPort
import com.teady.aibudgetserver.adapter.secondary.kafka.port.KafkaClientPort
import com.teady.aibudgetserver.domain.board.entity.Board
import com.teady.aibudgetserver.domain.board.executor.BoardExecutor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
@Slf4j
class BoardUseCase (
    private val boardExecutor: BoardExecutor,
    private val boardRepositoryPort: BoardRepositoryPort,
    private val kafkaClientPort: KafkaClientPort
) : WebBoardAdapterPort {
    override fun upload(boardDto : BoardDto) : HttpStatus {
        val dto = boardExecutor.doSomething(boardDto)
        boardRepositoryPort.save(dto.toEntity())
        kafkaClientPort.send("1", boardDto)
        return HttpStatus.OK
    }

    override fun items() : MutableIterable<BoardDto> {
        val boardDtolist: MutableIterable<Board> = boardRepositoryPort.findAll()
        return boardExecutor.doSomething(boardDtolist.map { b -> BoardDto.fromEntity(b) }.toMutableList())
    }
}