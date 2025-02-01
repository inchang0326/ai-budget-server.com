package com.teady.kp.adapter.primary.web

import com.teady.kp.adapter.primary.web.port.WebChatAdapterPort
import com.teady.kp.application.dto.ChatDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class WebChatAdapter(
    private val webChatAdapterPort: WebChatAdapterPort
) {
    @PostMapping("/question")
    fun ask(@RequestBody chatDto: ChatDto): Map<String, String> {
        return webChatAdapterPort.ask(chatDto)
    }
}