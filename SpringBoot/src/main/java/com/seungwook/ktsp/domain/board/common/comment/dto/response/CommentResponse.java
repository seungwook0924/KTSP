package com.seungwook.ktsp.domain.board.common.comment.dto.response;

import lombok.Getter;

@Getter
public class CommentResponse {
    private final long commentId;
    private final String commentWriterName;
    private final String content;

    public CommentResponse(long commentId, String commentWriterName, String content) {
        this.commentId = commentId;
        this.commentWriterName = commentWriterName;
        this.content = content;
    }
}

