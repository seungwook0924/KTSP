package com.seungwook.ktsp.domain.board.common.repository;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {

    // boardId를 바탕으로 작성자의 userId를 리턴(AccessHandler 최적화)
    @Query("SELECT b.user.id FROM Board b WHERE b.id = :boardId AND b.deleted = false")
    Optional<Long> findWriterIdById(Long boardId);

}
