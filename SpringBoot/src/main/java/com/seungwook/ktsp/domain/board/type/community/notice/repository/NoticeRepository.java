package com.seungwook.ktsp.domain.board.type.community.notice.repository;

import com.seungwook.ktsp.domain.board.common.repository.hits.HitsIncrement;
import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.type.community.notice.repository.querydsl.NoticeQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, HitsIncrement, NoticeQueryRepository{

    // boardId를 바탕으로 공지사항 조회
    Optional<Notice> findNoticeById(Long boardId);
}
