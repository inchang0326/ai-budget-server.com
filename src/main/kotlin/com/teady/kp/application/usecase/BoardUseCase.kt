package com.teady.kp.application.usecase

import com.teady.kp.application.dto.BoardDto
import com.teady.kp.adapter.primary.web.port.WebBoardAdapterPort
import com.teady.kp.adapter.secondary.jpa.port.BoardRepositoryPort
import com.teady.kp.adapter.secondary.kafka.port.KafkaProducerPort
import com.teady.kp.domain.board.entity.Board
import com.teady.kp.domain.board.executor.BoardExecutor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
@Slf4j
class BoardUseCase (
    private val boardExecutor: BoardExecutor,
    private val boardRepositoryPort: BoardRepositoryPort,
    private val kafkaProducerPort: KafkaProducerPort
) : WebBoardAdapterPort {
    override fun upload(boardDto : BoardDto) : HttpStatus {
        val dto = boardExecutor.doSomething(boardDto)
        boardRepositoryPort.save(dto.toEntity())
        kafkaProducerPort.send("1", boardDto)
        return HttpStatus.OK
    }

    override fun items() : MutableIterable<BoardDto> {
        val boardDtolist: MutableIterable<Board> = boardRepositoryPort.findAll()
        return boardExecutor.doSomething(boardDtolist.map { b -> BoardDto.fromEntity(b) }.toMutableList())
    }
}