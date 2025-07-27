package com.seungwook.ktsp.domain.board.function.etc.notice.repository;

import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findNoticeById(Long boardId);
}
