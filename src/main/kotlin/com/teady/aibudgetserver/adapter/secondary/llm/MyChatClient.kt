package com.teady.aibudgetserver.adapter.secondary.llm

import com.teady.aibudgetserver.adapter.secondary.llm.port.MyChatClientPort
import com.teady.aibudgetserver.application.dto.ChatDto
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.stereotype.Component

@Component
class MyChatClient(
    private val chatMemory: ChatMemory,
    private val ollamaChatModel: OllamaChatModel
) : MyChatClientPort {
    override fun answer(chatDto: ChatDto): String {
        val history: List<Message> = chatMemory.get(chatDto.conversationId, 100)

        val guidToSystem = """
            Your name is {name}
            Remeber {history} is the conversation history between user and you. 
            All of your replies should be summarized in 2-line without history.
            """.trimIndent()
        val systemPromptTemplate = SystemPromptTemplate(guidToSystem)
        val systemMessage: Message =
            systemPromptTemplate.createMessage(java.util.Map.of<String, Any>("name", "Teady.kang", "history", history))
        val userMessage: Message = UserMessage(chatDto.message)

        val prompt = Prompt(listOf(systemMessage, userMessage))

        chatMemory.add(chatDto.conversationId, userMessage)
        val answer: String = ollamaChatModel.call(prompt).result.output.content
        chatMemory.add(chatDto.conversationId, AssistantMessage(answer))
        return answer
    }
}