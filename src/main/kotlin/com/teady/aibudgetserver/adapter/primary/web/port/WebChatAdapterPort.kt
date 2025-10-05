package com.teady.aibudgetserver.adapter.primary.web.port

import com.teady.aibudgetserver.application.dto.ChatDto

interface WebChatAdapterPort {
    fun ask(chatDto: ChatDto): Map<String, String>
    fun prepare()
    fun askMore(chatDto: ChatDto): Map<String, String?>
}