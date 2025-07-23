package com.seungwook.ktsp.domain.board.function.etc.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardUpdateRequest {

    @Schema(description = "게시글 제목")
    @NotBlank
    @Size(min = 2, max = 50, message = "게시글 제목은 최소 2자 ~ 최대 50자 입니다.")
    private final String title;

    @Schema(description = "게시글 내용")
    @NotBlank
    @Size(min = 2, max = 10000, message = "게시글 내용은 최소 2자 이상, 최대 10000자 이하입니다.")
    private final String content;

    public BoardUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
