package com.teady.kp.application.usecase

import com.teady.kp.adapter.primary.web.port.WebChatAdapterPort
import com.teady.kp.adapter.secondary.chat.port.MyChatClientPort
import com.teady.kp.adapter.secondary.chat.port.MyRAGClientPort
import com.teady.kp.application.dto.ChatDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChatUseCase(
    private val myChatClientPort: MyChatClientPort,
    private val myRAGClientPort: MyRAGClientPort
) : WebChatAdapterPort {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatUseCase::class.java)
    }

    override fun ask(chatDto: ChatDto): Map<String, String> {
        return java.util.Map.of("answer", myChatClientPort.answer(chatDto))
    }

    override fun prepare() {
        myRAGClientPort.processedRAG()
    }

    override fun askMore(chatDto: ChatDto): Map<String, String?> {
        return java.util.Map.of("answer", myRAGClientPort.answerAdvanced(chatDto))
    }
}