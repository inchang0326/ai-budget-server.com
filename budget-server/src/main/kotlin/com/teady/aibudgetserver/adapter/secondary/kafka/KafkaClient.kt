package com.teady.aibudgetserver.adapter.secondary.kafka

import com.teady.aibudgetserver.adapter.secondary.kafka.port.KafkaClientPort
import com.teady.aibudgetserver.application.dto.BoardDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaClient(
    private var kafkatemplate: KafkaTemplate<String, BoardDto>
) : KafkaClientPort {
    override fun send(topic: String, boardDto: BoardDto) {
        kafkatemplate.send(topic, boardDto)
    }
}