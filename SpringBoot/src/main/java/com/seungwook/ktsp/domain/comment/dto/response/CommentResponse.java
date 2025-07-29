package com.seungwook.ktsp.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private final long commentId;

    private final Long parentCommentId;

    private final long userId;

    private final String commenter;

    private final String content;

    @JsonFormat(pattern = "yy.MM.dd-HH:mm:ss")
    private final LocalDateTime createdAt;

    private final boolean isEdited;

    private final boolean manageable;
}