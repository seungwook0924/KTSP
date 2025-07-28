package com.seungwook.ktsp.domain.board.type.community.notice.service;

import com.seungwook.ktsp.domain.board.common.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.type.community.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeQueryService {

    private final NoticeRepository noticeRepository;

    // 전체 공지사항 조회
    public Page<CommunityList> getAllNotice(int page) {

        // 최대 15개의 데이터 페이징
        return noticeRepository.findAllNoticePage(PageRequest.of(page, 15));
    }

    // 특정 공지사항 조회
    @Transactional
    public Notice getNotice(long boardId) {

        // 조회수 증가
        noticeRepository.increaseHits(boardId);

        // 공지사항 조회
        return findAsNotice(boardId);
    }

    // 공용 메서드
    private Notice findAsNotice(long boardId) {
        return noticeRepository.findNoticeById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
