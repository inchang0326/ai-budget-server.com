package com.teady.kp.adapter.primary.web.port

import com.teady.kp.application.dto.ChatDto

interface WebChatAdapterPort {
    fun ask(chatDto: ChatDto): Map<String, String>
    fun prepare()
    fun askMore(chatDto: ChatDto): Map<String, String?>
}