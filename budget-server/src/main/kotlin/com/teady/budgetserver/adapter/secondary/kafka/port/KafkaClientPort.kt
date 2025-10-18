package com.teady.budgetserver.adapter.secondary.kafka.port

import com.teady.budgetserver.application.dto.BoardDto

interface KafkaClientPort {
    fun send(topic: String, boardDto: BoardDto)
}