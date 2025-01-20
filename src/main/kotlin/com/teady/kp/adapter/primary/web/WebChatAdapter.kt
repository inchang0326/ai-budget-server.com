package com.teady.kp.adapter.primary.web

import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ai")
class WebChatAdapter(
    private val chatModel: OllamaChatModel
) {
    @GetMapping("/generate")
    fun generate(
        @RequestParam(
            value = "message",
            defaultValue = "Tell me a joke"
        ) message: String?
    ): Map<String, String> {
        return java.util.Map.of("generation", chatModel.call(message))
    }
}