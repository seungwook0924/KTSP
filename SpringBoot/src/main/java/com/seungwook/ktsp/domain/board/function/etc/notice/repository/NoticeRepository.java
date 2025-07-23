package com.seungwook.ktsp.domain.board.function.etc.notice.repository;

import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 공지사항 조회
    @Query("SELECT f FROM Notice f WHERE f.id = :id AND f.deleted = false")
    Optional<Notice> findNoticeById(@Param("id") Long id);
}
