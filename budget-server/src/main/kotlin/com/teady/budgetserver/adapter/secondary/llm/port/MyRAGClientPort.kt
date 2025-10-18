package com.teady.budgetserver.adapter.secondary.llm.port

import com.teady.budgetserver.application.dto.ChatDto

interface MyRAGClientPort {
    fun processedRAG()
    fun answerAdvanced(chatDto: ChatDto): String?
}