package com.teady.kp.repository.dao

import com.teady.kp.repository.data.entity.Board
import org.springframework.data.repository.CrudRepository

interface BoardRepository : CrudRepository<Board, Integer>