package com.teady.kp.adapter.secondary.chat.port

import com.teady.kp.application.dto.ChatDto

interface MyRAGClientPort {
    fun processedRAG()
    fun answerAdvanced(chatDto: ChatDto): String?
}