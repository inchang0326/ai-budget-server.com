package com.teady.budgetserver.adapter.secondary.jpa.board

import com.teady.budgetserver.domain.board.entity.Board
import org.springframework.data.repository.CrudRepository

interface JpaBoardRepository : CrudRepository<Board, Integer>