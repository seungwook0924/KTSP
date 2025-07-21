package com.seungwook.ktsp.domain.board.function.etc.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;

    public RegisterRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
