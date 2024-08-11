package com.teady.kp.controller

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/kafka")
class PublishController(
    private var kafkaTemplate: KafkaTemplate<String, String>
) {
    @PostMapping("/publish")
    fun publish() : CompletableFuture<SendResult<String, String>> = kafkaTemplate.send("1", "Hello, World!")
}