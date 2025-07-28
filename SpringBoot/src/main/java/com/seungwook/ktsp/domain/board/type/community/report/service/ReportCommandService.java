package com.seungwook.ktsp.domain.board.type.community.report.service;

import com.seungwook.ktsp.domain.board.common.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityRegisterRequest;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityUpdateRequest;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.board.type.community.report.repository.ReportRepository;
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
public class ReportCommandService {

    private final ReportRepository reportRepository;
    private final UserDomainService userDomainService;
    private final BoardFileBindingService boardFileBindingService;

    // 리포트 등록
    @Transactional
    public void registerReport(long userId, CommunityRegisterRequest request) {

        // 작성자 조회
        User user = userDomainService.getReferenceById(userId);

        // 리포트 생성
        Report report = Report.createReport(user, request.getTitle(), request.getContent());
        reportRepository.save(report);

        // 이미지 및 첨부파일 연결
        if (request.getAttachedFiles() != null && !request.getAttachedFiles().isEmpty())
            boardFileBindingService.bindFilesToBoard(report, request.getContent(), request.getAttachedFiles());
    }

    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void updateReport(long boardId, CommunityUpdateRequest request) {

        // 리포트 조회
        Report report = findAsReport(boardId);

        // 이미지 및 첨부파일 수정 반영
        boardFileBindingService.updateBoundFiles(report, request.getContent(), request.getAttachedFiles());

        // 리포트 업데이트
        report.updateReport(report.getTitle(), request.getContent());
    }

    // 공지사항 삭제
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void deleteReport(long boardId) {

        // 리포트 조회
        Report report = findAsReport(boardId);

        // 연결된 파일 삭제
        boardFileBindingService.deleteBoundFiles(report);

        // 리포트 삭제
        reportRepository.delete(report);
    }

    // 공용 메서드
    private Report findAsReport(long boardId) {
        return reportRepository.findReportById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
