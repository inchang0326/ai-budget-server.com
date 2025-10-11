package com.teady.aibudgetserver.adapter.secondary.jpa.board

import com.teady.aibudgetserver.domain.board.entity.Board
import org.springframework.data.repository.CrudRepository

interface JpaBoardRepository : CrudRepository<Board, Integer>