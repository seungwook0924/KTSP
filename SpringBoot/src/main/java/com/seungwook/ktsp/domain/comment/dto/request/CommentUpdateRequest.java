package com.seungwook.ktsp.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank
    @Size(max = 255, message = "댓글은 최대 255 글자까지 입력할 수 있습니다.")
    private final String comment;

    public CommentUpdateRequest(String comment) {
        this.comment = comment;
    }
}
