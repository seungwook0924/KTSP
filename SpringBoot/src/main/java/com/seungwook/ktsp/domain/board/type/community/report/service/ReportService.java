package com.seungwook.ktsp.domain.board.type.community.report.service;

import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityRegisterRequest;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.board.type.community.report.repository.ReportRepository;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserDomainService userDomainService;
    private final BoardFileBindingService boardFileBindingService;

    // 리포트 등록
    @Transactional
    public void registerReport(long userId, CommunityRegisterRequest request) {

        // 작성자 조회
        User user = userDomainService.getReferenceById(userId);

        Report report = Report.createReport(user, request.getTitle(), request.getContent());
        reportRepository.save(report);

        // 이미지 및 첨부파일 연결
        boardFileBindingService.bindFilesToBoard(report, request.getContent(), request.getAttachedFiles());
    }

    // 본인이 작성한 리포트 조회
    public Page<CommunityList> getUserReports(long userId, int page) {

        // 최대 15개의 데이터 페이징
        return reportRepository.getUserReports(userId, PageRequest.of(page, 15));
    }
}
