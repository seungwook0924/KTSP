package com.seungwook.ktsp.domain.comment.repository.querydsl;

import com.seungwook.ktsp.domain.comment.dto.CommentInfo;

import java.util.List;

public interface CommentQueryRepository {

    // 특정 Board에 있는 모든 Comment 반환
    List<CommentInfo> getCommentsByBoardId(Long boardId);
}
