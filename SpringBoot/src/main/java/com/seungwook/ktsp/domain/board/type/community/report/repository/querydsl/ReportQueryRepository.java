package com.seungwook.ktsp.domain.board.type.community.report.repository.querydsl;

import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportQueryRepository {

    // 본인이 작성한 모든 리포트 리턴
    Page<CommunityList> getUserReports(Long userId, Pageable pageable);

    // 관리자 전용 모든 리포트 리턴
    Page<CommunityList> getAllReports(Pageable pageable);
}
