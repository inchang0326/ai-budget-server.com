package com.teady.kp.adapter.secondary.kafka.port

import com.teady.kp.application.dto.BoardDto

interface KafkaProducerPort {
    fun send(topic: String, boardDto: BoardDto)
}