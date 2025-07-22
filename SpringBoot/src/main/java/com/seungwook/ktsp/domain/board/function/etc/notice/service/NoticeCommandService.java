package com.seungwook.ktsp.domain.board.function.etc.notice.service;

import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardRegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardUpdateRequest;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeCommandService {

    private final NoticeDomainService noticeDomainService;
    private final UserDomainService userDomainService;

    @Transactional
    public void noticeRegister(long userId, BoardRegisterRequest request) {

        User user = userDomainService.getReferenceById(userId);

        Notice notice = Notice.createNotice(user, request.getTitle(), request.getContent());

        noticeDomainService.save(notice);
    }

    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void noticeUpdate(long boardId, BoardUpdateRequest request) {
        Notice notice = noticeDomainService.findAsNotice(boardId);

        notice.updateNotice(request.getTitle(), request.getContent());
    }
}
