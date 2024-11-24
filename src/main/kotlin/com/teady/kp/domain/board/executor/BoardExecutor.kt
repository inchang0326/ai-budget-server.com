package com.teady.kp.domain.board.executor

import com.teady.kp.domain.board.repository.BoardRepository
import com.teady.kp.application.dto.BoardDto
import com.teady.kp.domain.board.entity.Board
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
@Slf4j
class BoardExecutor(
    private val boardRepository: BoardRepository,
    private var kafkaTemplate: KafkaTemplate<String, BoardDto>
) {
    fun upload(boardDto : BoardDto) : HttpStatus {
        boardRepository.save(boardDto.toEntity())
        kafkaTemplate.send("1", boardDto)
        return HttpStatus.OK
    }

    fun items() : MutableIterable<Board> {
        return boardRepository.findAll()
    }
}