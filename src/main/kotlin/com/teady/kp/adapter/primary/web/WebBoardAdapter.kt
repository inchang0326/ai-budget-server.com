package com.teady.kp.adapter.primary.web

import com.teady.kp.adapter.primary.web.port.WebBoardPort
import com.teady.kp.application.dto.BoardDto
import com.teady.kp.application.usecase.BoardUseCase
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/board")
class WebBoardAdapter (
    private val boardUseCase: BoardUseCase
) : WebBoardPort {

    @PostMapping("/upload")
    override fun upload(@RequestBody boardDto: BoardDto) : HttpStatus {
        boardUseCase.upload(boardDto)
        return HttpStatus.OK
    }

    @GetMapping("/items")
    override fun items() : List<BoardDto> = boardUseCase.items()
}