package com.teady.kp.application.usecase

import com.teady.kp.adapter.primary.web.port.WebChatAdapterPort
import com.teady.kp.adapter.secondary.chat.port.ChatClientPort
import com.teady.kp.application.dto.ChatDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.stereotype.Service

@Service
class ChatUseCase(
    private val chatClientPort: ChatClientPort
) : WebChatAdapterPort {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatUseCase::class.java)
    }

    override fun ask(chatDto: ChatDto): Map<String, String> {
        return java.util.Map.of("answer", chatClientPort.answer(chatDto))
    }
}