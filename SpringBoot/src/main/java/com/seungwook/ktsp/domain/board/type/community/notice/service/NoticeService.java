package com.seungwook.ktsp.domain.board.type.community.notice.service;

import com.seungwook.ktsp.domain.board.common.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityRegisterRequest;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityUpdateRequest;
import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.type.community.notice.repository.NoticeRepository;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public void registerNotice(long userId, CommunityRegisterRequest request) {

        // 작성자 조회
        User user = userDomainService.getReferenceById(userId);

        // 공지사항 생성
        Notice notice = Notice.createNotice(user, request.getTitle(), request.getContent());
        noticeRepository.save(notice);

        // 이미지 및 첨부파일 연결
        boardFileBindingService.bindFilesToBoard(notice, request.getContent(), request.getAttachedFiles());
    }

    // 전체 공지사항 조회
    public Page<CommunityList> getAllNotice(int page) {

        // 최대 15개의 데이터 페이징
        return noticeRepository.findAllNoticePage(PageRequest.of(page, 15));
    }

    // 공지사항 조회
    @Transactional
    public Notice getNotice(long boardId) {

        // 조회수 증가
        noticeRepository.increaseHits(boardId);

        // 공지사항 조회
        return findAsNotice(boardId);
    }

    // 공지사항 수정
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void updateNotice(long boardId, CommunityUpdateRequest request) {

        // 공지사항 조회
        Notice notice = findAsNotice(boardId);

        // 이미지 및 첨부파일 수정 반영
        boardFileBindingService.updateBoundFiles(notice, request.getContent(), request.getAttachedFiles());

        // 공지사항 업데이트
        notice.updateNotice(request.getTitle(), request.getContent());
    }

    // 공지사항 삭제
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void deleteNotice(long boardId) {

        // 공지사항 조회
        Notice notice = findAsNotice(boardId);

        // 연결된 파일 삭제
        boardFileBindingService.deleteBoundFiles(notice);

        // 공지사항 삭제
        noticeRepository.delete(notice);
    }



    // 공용 메서드
    private Notice findAsNotice(long boardId) {
        return noticeRepository.findNoticeById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
