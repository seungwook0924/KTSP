package com.seungwook.ktsp.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentInfo {
    private final long userId;
    private final String writer;
    private final long commentId;
    private final Long parentCommentId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
