package com.teady.budgetserver.domain.board.executor

import com.teady.budgetserver.application.dto.BoardDto
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Service
@Slf4j
class BoardExecutor { // 도메인 로직은 Port&Adapter 및 Application 계층에서 자유로움
    fun doSomething(boardDto : BoardDto) : BoardDto {
        return boardDto
    }

    fun doSomething(boardDtoList: MutableIterable<BoardDto>) : MutableIterable<BoardDto> {
        return boardDtoList
    }
}