package com.zeroone.ktsp.repository;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.domain.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long>
{
    boolean existsByBoardAndUser(Board board, User user);

    Optional<Waiting> findByBoard(Board board); // Board를 기반으로 Waiting을 조회하는 메서드

    List<Waiting> findAllByBoard(Board board); // Board를 기반으로 모든 Waiting 객체를 조회

    Optional<Waiting> findById(long id); // id를 기반으로 Waiting 객체를 조회
}
