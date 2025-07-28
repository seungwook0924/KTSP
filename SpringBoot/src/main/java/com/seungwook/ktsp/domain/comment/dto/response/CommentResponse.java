package com.seungwook.ktsp.domain.comment.dto.response;

import lombok.Getter;

@Getter
public class CommentResponse {
    private final long commentId;
    private final Long parentCommentId;
    private final String commentWriterName;
    private final String content;

    public CommentResponse(long commentId, Long parentCommentId, String commentWriterName, String content) {
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.commentWriterName = commentWriterName;
        this.content = content;
    }
}