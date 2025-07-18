package com.seungwook.ktsp.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WithdrawnRequest {

    @Schema(description = "사용자 비밀번호", example = "SecurePass123!")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 최소 8자 ~ 최대 30자 입니다.")
    private final String password;

    public WithdrawnRequest(String password) {
        this.password = password;
    }
}
