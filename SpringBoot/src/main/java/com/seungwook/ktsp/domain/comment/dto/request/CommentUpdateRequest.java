package com.seungwook.ktsp.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentUpdateRequest {

    @Positive(message = "잘못된 접근입니다.")
    private final long commentId;

    @NotBlank
    @Size(max = 255, message = "댓글은 최대 255 글자까지 입력할 수 있습니다.")
    private final String comment;
}
