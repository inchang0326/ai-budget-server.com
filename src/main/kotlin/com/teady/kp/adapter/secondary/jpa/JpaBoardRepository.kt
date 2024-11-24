package com.teady.kp.adapter.secondary.jpa

import com.teady.kp.domain.board.entity.Board
import org.springframework.data.repository.CrudRepository

interface JpaBoardRepository : CrudRepository<Board, Integer>