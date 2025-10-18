package com.teady.aibudgetserver.adapter.secondary.llm.port

import com.teady.aibudgetserver.application.dto.ChatDto

interface MyChatClientPort {
    fun answer(chatDto: ChatDto): String
}