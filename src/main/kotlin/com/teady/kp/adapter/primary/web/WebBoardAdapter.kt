package com.teady.kp.adapter.primary.web

import com.teady.kp.application.dto.BoardDto
import com.teady.kp.adapter.primary.web.port.WebBoardAdapterPort
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

    @PostMapping("/upload")
    fun upload(@RequestBody boardDto: BoardDto) : HttpStatus {
        webBoardAdapterPort.upload(boardDto)
        return HttpStatus.OK
    }

    @GetMapping("/items")
    fun items() : List<BoardDto> = webBoardAdapterPort.items()
}