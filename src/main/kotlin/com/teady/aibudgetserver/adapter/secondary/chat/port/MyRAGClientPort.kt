package com.teady.aibudgetserver.adapter.secondary.chat.port

import com.teady.aibudgetserver.application.dto.ChatDto

interface MyRAGClientPort {
    fun processedRAG()
    fun answerAdvanced(chatDto: ChatDto): String?
}