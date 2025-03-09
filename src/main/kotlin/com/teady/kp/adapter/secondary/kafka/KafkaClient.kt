package com.teady.kp.adapter.secondary.kafka

import com.teady.kp.adapter.secondary.kafka.port.KafkaClientPort
import com.teady.kp.application.dto.BoardDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaClient(
    private var kafkatemplate: KafkaTemplate<String, BoardDto>
) : KafkaClientPort{
    override fun send(topic: String, boardDto: BoardDto) {
        kafkatemplate.send(topic, boardDto)
    }
}