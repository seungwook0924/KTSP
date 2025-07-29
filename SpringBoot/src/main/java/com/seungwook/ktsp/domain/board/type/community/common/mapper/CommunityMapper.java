package com.seungwook.ktsp.domain.board.type.community.common.mapper;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import com.seungwook.ktsp.domain.board.type.community.common.dto.response.CommunityListResponse;
import com.seungwook.ktsp.domain.board.type.community.common.dto.response.CommunityResponse;
import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class CommunityMapper {

    public static Page<CommunityListResponse> toNoticeList(Page<CommunityList> communityLists) {

        int total = (int) communityLists.getTotalElements(); // 전체 글 수
        int pageNumber = communityLists.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int pageSize = communityLists.getSize(); // 페이지당 개수


        int startNumber = total - (pageNumber * pageSize);   // 현재 페이지에서 시작할 number

        List<CommunityListResponse> content = new ArrayList<>();

        for (int i = 0; i < communityLists.getContent().size(); i++) {
            CommunityList dto = communityLists.getContent().get(i);
            content.add(new CommunityListResponse(
                    startNumber - i, // 역순
                    dto.getBoardId(),
                    dto.getTitle(),
                    dto.getHits(),
                    "관리자",
                    dto.getCreatedAt()
            ));
        }

        return new PageImpl<>(content, communityLists.getPageable(), communityLists.getTotalElements());
    }

    public static CommunityResponse toNoticeResponse(Writer writer, Notice notice, List<AttachedFileInfo> attachedFileInfos, boolean manageable) {
        return new CommunityResponse(
                writer,
                notice.getId(),
                notice.getHits(),
                notice.getTitle(),
                notice.getContent(),
                manageable, // 수정 및 삭제 권한 보유 여부
                notice.getCreatedAt(),
                notice.getModifiedAt(),
                attachedFileInfos,
                List.of() // 공지사항은 댓글 없음
        );
    }

    public static Page<CommunityListResponse> toReportList(Page<CommunityList> communityLists) {

        int total = (int) communityLists.getTotalElements(); // 전체 글 수
        int pageNumber = communityLists.getNumber(); // 현재 페이지 번호 (0부터 시작)
        int pageSize = communityLists.getSize(); // 페이지당 개수


        int startNumber = total - (pageNumber * pageSize);   // 현재 페이지에서 시작할 number

        List<CommunityListResponse> content = new ArrayList<>();

        for (int i = 0; i < communityLists.getContent().size(); i++) {
            CommunityList dto = communityLists.getContent().get(i);
            content.add(new CommunityListResponse(
                    startNumber - i, // 역순
                    dto.getBoardId(),
                    dto.getTitle(),
                    dto.getHits(),
                    dto.getWriter(),
                    dto.getCreatedAt()
            ));
        }

        return new PageImpl<>(content, communityLists.getPageable(), communityLists.getTotalElements());
    }

    public static CommunityResponse toReportResponse(Writer writer, Report report, List<AttachedFileInfo> attachedFileInfos, boolean manageable, List<CommentResponse> comments) {
        return new CommunityResponse(
                writer,
                report.getId(),
                report.getHits(),
                report.getTitle(),
                report.getContent(),
                manageable, // 수정 및 삭제 권한 보유 여부
                report.getCreatedAt(),
                report.getModifiedAt(),
                attachedFileInfos,
                comments
        );
    }
}