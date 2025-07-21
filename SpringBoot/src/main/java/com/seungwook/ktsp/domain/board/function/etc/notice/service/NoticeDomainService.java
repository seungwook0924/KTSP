package com.seungwook.ktsp.domain.board.function.etc.notice.service;

import com.seungwook.ktsp.domain.board.common.board.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.function.etc.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeDomainService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public Notice findAsNotice(long boardId) {
        return noticeRepository.findNoticeById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
