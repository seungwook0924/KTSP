package com.seungwook.ktsp.domain.board.common.repository;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
