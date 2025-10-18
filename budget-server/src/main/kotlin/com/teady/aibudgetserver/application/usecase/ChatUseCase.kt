package com.teady.aibudgetserver.application.usecase

import com.teady.aibudgetserver.adapter.primary.web.port.WebChatAdapterPort
import com.teady.aibudgetserver.adapter.secondary.llm.port.MyChatClientPort
import com.teady.aibudgetserver.adapter.secondary.llm.port.MyRAGClientPort
import com.teady.aibudgetserver.application.dto.ChatDto
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