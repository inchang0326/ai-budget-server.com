package com.teady.aibudgetserver.adapter.secondary.kafka.port

import com.teady.aibudgetserver.application.dto.BoardDto

interface KafkaClientPort {
    fun send(topic: String, boardDto: BoardDto)
}