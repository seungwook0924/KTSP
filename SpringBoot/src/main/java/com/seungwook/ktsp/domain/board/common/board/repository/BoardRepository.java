package com.seungwook.ktsp.domain.board.common.board.repository;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // boardId를 바탕으로 board의 PK를 리턴(AccessHadler 최적화)
    @Query("SELECT b.user.id FROM Board b WHERE b.id = :id AND b.deleted = false")
    Long findWriterIdById(@Param("id") Long boardId);

}
