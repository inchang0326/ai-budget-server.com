package com.teady.kp.adapter.primary.web.port

import com.teady.kp.application.dto.BoardDto
import org.springframework.http.HttpStatus

interface WebBoardPort {
    fun upload(boardDto: BoardDto) : HttpStatus
    fun items() : List<BoardDto>
}