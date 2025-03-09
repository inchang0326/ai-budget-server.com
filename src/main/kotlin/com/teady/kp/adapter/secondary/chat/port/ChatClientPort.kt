package com.teady.kp.adapter.secondary.chat.port

import com.teady.kp.application.dto.ChatDto

interface ChatClientPort {
    fun answer(chatDto: ChatDto): String
}