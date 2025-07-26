package com.seungwook.ktsp.domain.board.function.etc.common.mapper;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.response.BoardResponse;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;

import java.util.List;

public class EtcResponseMapper {

    public static BoardResponse toNoticeResponse(Writer writer, Notice notice, List<AttachedFileInfo> attachedFileInfos) {
        return new BoardResponse(
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


//    public static BoardResponse toReportResponse(Board board, List<Comment> comments) {
//        List<CommentResponse> commentResponses = comments.stream()
//                .map(c -> new CommentResponse(
//                        c.getId(),
//                        c.getUser().getName(), // 또는 닉네임
//                        c.getComment()
//                ))
//                .toList();
//
//        return new BoardResponse(
//                board.getId(),
//                board.getTitle(),
//                board.getContent(),
//                board.getCreatedAt(),
//                board.getModifiedAt(),
//                commentResponses
//        );
//    }
}