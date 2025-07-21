package com.seungwook.ktsp.domain.board.function.etc.common.mapper;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.response.BoardResponse;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;

import java.util.List;

public class EtcResponseMapper {

    public static BoardResponse toNoticeResponse(Notice notice) {
        return new BoardResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getModifiedAt(),
                List.of() // 공지사항은 댓글 없음
        );
    }


    public static BoardResponse toReportResponse(Board board, List<Comment> comments) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getUser().getName(), // 또는 닉네임
                        c.getComment()
                ))
                .toList();

        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getModifiedAt(),
                commentResponses
        );
    }
}