package com.teady.kp.adapter.secondary.kafka

import com.teady.kp.adapter.secondary.kafka.port.KafkaProducerPort
import com.teady.kp.application.dto.BoardDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private var kafkatemplate: KafkaTemplate<String, BoardDto>
) : KafkaProducerPort{
    override fun send(topic: String, boardDto: BoardDto) {
        kafkatemplate.send(topic, boardDto)
    }
}