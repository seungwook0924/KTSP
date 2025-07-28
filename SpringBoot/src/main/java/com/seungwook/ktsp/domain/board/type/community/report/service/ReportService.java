package com.seungwook.ktsp.domain.board.type.community.report.service;

import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.type.community.report.repository.ReportRepository;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserDomainService userDomainService;
    private final BoardFileBindingService boardFileBindingService;


}
