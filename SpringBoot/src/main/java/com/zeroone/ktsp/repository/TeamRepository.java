package com.zeroone.ktsp.repository;

import com.zeroone.ktsp.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long>
{
    // 같은 board_id에 매핑된 user 수 계산
    @Query("SELECT COUNT(t.user.id) FROM Team t WHERE t.board.id = :boardId")
    Byte countUsersByBoardId(@Param("boardId") long boardId);

    List<Team> findAllByBoardId(Long boardId); // 같은 board에 매핑된 모든 Team 객체를 가져오는 메서드
}
