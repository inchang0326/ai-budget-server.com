package com.teady.budgetserver.adapter.primary.web.port

import com.teady.budgetserver.application.dto.BoardDto
import org.springframework.http.HttpStatus

interface WebBoardAdapterPort {
    fun upload(boardDto : BoardDto) : HttpStatus
    fun items() : MutableIterable<BoardDto>
}