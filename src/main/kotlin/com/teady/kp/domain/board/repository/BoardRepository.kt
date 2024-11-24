package com.teady.kp.domain.board.repository

import com.teady.kp.domain.board.entity.Board
import org.springframework.data.repository.CrudRepository

interface BoardRepository : CrudRepository<Board, Integer>