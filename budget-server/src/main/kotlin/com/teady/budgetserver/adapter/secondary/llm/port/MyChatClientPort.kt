package com.teady.budgetserver.adapter.secondary.llm.port

import com.teady.budgetserver.application.dto.ChatDto

interface MyChatClientPort {
    fun answer(chatDto: ChatDto): String
}