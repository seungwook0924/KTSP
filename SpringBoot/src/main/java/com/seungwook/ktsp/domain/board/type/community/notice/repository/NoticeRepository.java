package com.seungwook.ktsp.domain.board.type.community.notice.repository;

import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.type.community.notice.repository.querydsl.NoticeQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeQueryRepository {

    Optional<Notice> findNoticeById(Long boardId);
}
