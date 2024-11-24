package com.teady.kp.adapter.secondary.kafka.port

import com.teady.kp.application.dto.BoardDto
import org.springframework.kafka.core.KafkaTemplate
import java.io.Serializable

interface KafkaPort {
    fun kafkaTemplate(): KafkaTemplate<String, BoardDto>
    fun producerConfigs(): Map<String, Serializable>
}