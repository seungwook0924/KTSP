package com.seungwook.ktsp.domain.board.type.community.report.service;

import com.seungwook.ktsp.domain.board.common.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.board.type.community.report.repository.ReportRepository;
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
public class ReportQueryService {

    private final ReportRepository reportRepository;

    // 본인이 작성한 리포트 조회
    public Page<CommunityList> getUserReports(long userId, int page) {

        // 최대 15개의 데이터 페이징
        return reportRepository.getUserReports(userId, PageRequest.of(page, 15));
    }

    // 관리자 전용 모든 리포트 조회
    public Page<CommunityList> getAllReports(int page) {

        // 최대 15개의 데이터 페이징
        return reportRepository.getAllReports(PageRequest.of(page, 15));
    }

    // 리포트 조회
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public Report getReport(long boardId) {

        reportRepository.increaseHits(boardId);

        return findAsReport(boardId);
    }

    // 공용 메서드
    private Report findAsReport(long boardId) {
        return reportRepository.findReportById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
