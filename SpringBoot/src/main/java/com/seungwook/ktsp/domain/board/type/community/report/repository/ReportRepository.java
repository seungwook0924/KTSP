package com.seungwook.ktsp.domain.board.type.community.report.repository;

import com.seungwook.ktsp.domain.board.common.repository.hits.HitsIncrement;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.board.type.community.report.repository.querydsl.ReportQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>, HitsIncrement, ReportQueryRepository {

    // boardId를 바탕으로 공지사항 조회
    Optional<Report> findReportById(Long boardId);
}
