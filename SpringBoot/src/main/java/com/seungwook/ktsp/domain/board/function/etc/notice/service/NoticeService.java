package com.seungwook.ktsp.domain.board.function.etc.notice.service;

import com.seungwook.ktsp.domain.board.common.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardRegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardUpdateRequest;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.function.etc.notice.repository.NoticeRepository;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserDomainService userDomainService;
    private final BoardFileBindingService boardFileBindingService;

    // 공지사항 등록
    @Transactional
    public void registerNotice(long userId, BoardRegisterRequest request) {

        // 작성자 조회
        User user = userDomainService.getReferenceById(userId);

        // 공지사항 생성
        Notice notice = Notice.createNotice(user, request.getTitle(), request.getContent());
        noticeRepository.save(notice);

        // 이미지 및 첨부파일을 Notice와 연결
        boardFileBindingService.bindFilesToBoard(notice, request.getContent(), request.getAttachedFiles());
    }

    // 공지사항 조회
    @Transactional(readOnly = true)
    public Notice getNotice(long boardId) {
        return findAsNotice(boardId);
    }

    // 공지사항 수정
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void updateNotice(long boardId, BoardUpdateRequest request) {

        Notice notice = findAsNotice(boardId);

        // 이미지 및 첨부파일을 공지사항과 연결
        boardFileBindingService.updateBoundFiles(notice, request.getContent(), request.getAttachedFiles());

        notice.updateNotice(request.getTitle(), request.getContent());
    }

    // 공지사항 삭제
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void deleteNotice(long boardId) {

        Notice notice = findAsNotice(boardId);

        // 파일 등 삭제 로직

        noticeRepository.delete(notice);
    }

    // 공용 메서드
    private Notice findAsNotice(long boardId) {
        return noticeRepository.findNoticeById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
