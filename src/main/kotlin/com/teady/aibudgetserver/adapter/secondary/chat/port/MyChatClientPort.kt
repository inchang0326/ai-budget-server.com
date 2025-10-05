package com.teady.aibudgetserver.adapter.secondary.chat.port

import com.teady.aibudgetserver.application.dto.ChatDto

interface MyChatClientPort {
    fun answer(chatDto: ChatDto): String
}