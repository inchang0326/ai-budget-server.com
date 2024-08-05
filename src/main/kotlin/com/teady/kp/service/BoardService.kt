package com.teady.kp.service

import com.teady.kp.repository.dao.BoardRepository
import com.teady.kp.repository.data.dto.BoardDto
import com.teady.kp.repository.data.entity.Board
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
@Slf4j
class BoardService(
    private val boardRepository: BoardRepository
) {
    fun upload(boardDto : BoardDto) : HttpStatus {
        boardRepository.save(boardDto.toEntity())
        return HttpStatus.OK
    }

    fun items() : List<BoardDto> {
        val list: MutableIterable<Board> = boardRepository.findAll()
        return list.map { b -> BoardDto.fromEntity(b) }.toList()
    }
}