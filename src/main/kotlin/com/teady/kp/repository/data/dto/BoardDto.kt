package com.teady.kp.repository.data.dto

import com.teady.kp.repository.data.entity.Board

data class BoardDto(val uuid : Long, val author : String, val contents : String, val emotion : String, val createdAt : String?) {
    fun toEntity(): Board = Board(author = author, contents = contents, emotion = emotion)

    companion object {
        fun fromEntity(board: Board): BoardDto = BoardDto(
            uuid = board.boardId,
            author = board.author,
            contents = board.contents,
            emotion = board.emotion,
            createdAt = board.createdAt
        )
    }
}