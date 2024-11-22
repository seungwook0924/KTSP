package com.zeroone.ktsp.repository;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.enumeration.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByType(BoardType type); // 특정 BoardType에 해당하는 모든 게시글 조회
}
