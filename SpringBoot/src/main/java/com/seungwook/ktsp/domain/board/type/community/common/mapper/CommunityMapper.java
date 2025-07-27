package com.seungwook.ktsp.domain.board.type.community.common.mapper;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.board.type.community.common.dto.response.CommunityResponse;
import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;

import java.util.List;

public class CommunityMapper {

    public static CommunityResponse toNoticeResponse(Writer writer, Notice notice, List<AttachedFileInfo> attachedFileInfos) {
        return new CommunityResponse(
                writer,
                notice.getId(),
                notice.getHits(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getModifiedAt(),
                List.of(), // 공지사항은 댓글 없음
                attachedFileInfos
        );
    }
}