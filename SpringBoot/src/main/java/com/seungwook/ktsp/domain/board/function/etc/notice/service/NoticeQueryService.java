package com.seungwook.ktsp.domain.board.function.etc.notice.service;

import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeQueryService {

    private final NoticeDomainService noticeDomainService;

    @Transactional(readOnly = true)
    public Notice getNotice(long boardId) {
        return noticeDomainService.findAsNotice(boardId);
    }
}
