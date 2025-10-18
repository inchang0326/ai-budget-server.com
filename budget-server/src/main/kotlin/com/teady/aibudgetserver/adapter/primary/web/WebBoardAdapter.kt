package com.teady.aibudgetserver.adapter.primary.web

import com.teady.aibudgetserver.application.dto.BoardDto
import com.teady.aibudgetserver.adapter.primary.web.port.WebBoardAdapterPort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/board")
class WebBoardAdapter (
    private val webBoardAdapterPort: WebBoardAdapterPort
) {

    @PostMapping("/item")
    fun upload(@RequestBody boardDto: BoardDto) : HttpStatus {
        webBoardAdapterPort.upload(boardDto)
        return HttpStatus.OK
    }

    @GetMapping("/items")
    fun retrieve() : MutableIterable<BoardDto> = webBoardAdapterPort.items()
}