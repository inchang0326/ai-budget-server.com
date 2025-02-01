package com.teady.kp.application.usecase

import com.teady.kp.adapter.primary.web.port.WebChatAdapterPort
import com.teady.kp.application.dto.ChatDto
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.stereotype.Service

@Service
class ChatUseCase (
    private val ollamaChatModel: OllamaChatModel
): WebChatAdapterPort {
    override fun ask(chatDto: ChatDto): Map<String, String> {
        return java.util.Map.of("answer", ollamaChatModel.call(chatDto.message))
    }
}