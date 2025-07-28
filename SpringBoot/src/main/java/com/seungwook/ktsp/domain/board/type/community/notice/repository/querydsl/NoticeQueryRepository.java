package com.seungwook.ktsp.domain.board.type.community.notice.repository.querydsl;

import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeQueryRepository {

    // 모든 공지사항 조회(페이징)
    Page<CommunityList> findAllNoticePage(Pageable pageable);

}
