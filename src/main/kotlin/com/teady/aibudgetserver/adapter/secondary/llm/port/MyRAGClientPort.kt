package com.teady.aibudgetserver.adapter.secondary.llm.port

import com.teady.aibudgetserver.application.dto.ChatDto

interface MyRAGClientPort {
    fun processedRAG()
    fun answerAdvanced(chatDto: ChatDto): String?
}