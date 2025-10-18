package com.teady.aibudgetserver.adapter.primary.web.port

import com.teady.aibudgetserver.application.dto.BoardDto
import org.springframework.http.HttpStatus

interface WebBoardAdapterPort {
    fun upload(boardDto : BoardDto) : HttpStatus
    fun items() : MutableIterable<BoardDto>
}