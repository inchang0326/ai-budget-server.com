package com.teady.kp.adapter.secondary.kafka.port

import com.teady.kp.application.dto.BoardDto

interface KafkaClientPort {
    fun send(topic: String, boardDto: BoardDto)
}