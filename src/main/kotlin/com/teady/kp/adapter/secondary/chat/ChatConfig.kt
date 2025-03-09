package com.teady.kp.adapter.secondary.chat

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatConfig {
    @Bean
    fun chatMemory(): ChatMemory = InMemoryChatMemory()
}