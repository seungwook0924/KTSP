package com.seungwook.ktsp.domain.board.function.etc.notice.service;

import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardRegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardUpdateRequest;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.function.etc.notice.service.domain.NoticeDomainService;
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
    private final BoardFileBindingService boardFileBindingService;

    // 공지사항 등록
    @Transactional
    public void registerNotice(long userId, BoardRegisterRequest request) {

        // 작성자 조회
        User user = userDomainService.getReferenceById(userId);

        // 공지사항 생성
        Notice notice = Notice.createNotice(user, request.getTitle(), request.getContent());
        noticeDomainService.save(notice);

        // 이미지 및 첨부파일을 공지사항과 연결
        boardFileBindingService.bindFilesToBoard(notice, request.getContent(), request.getAttachedFiles());
    }

    // 공지사항 수정
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void updateNotice(long boardId, BoardUpdateRequest request) {

        Notice notice = noticeDomainService.findAsNotice(boardId);

        notice.updateNotice(request.getTitle(), request.getContent());
    }

    // 공지사항 삭제
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void deleteNotice(long boardId) {

        Notice notice = noticeDomainService.findAsNotice(boardId);

        noticeDomainService.delete(notice);
    }
}
