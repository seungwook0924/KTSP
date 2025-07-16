package com.seungwook.ktsp.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordResetRequest {

    @Schema(description = "이메일", example = "user123@kangwon.ac.kr")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Size(min = 16, max = 40, message = "이메일은 최소 16자 ~ 최대 40자 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private final String email;

    @Schema(description = "사용자 비밀번호", example = "SecurePass123!")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 최소 8자 ~ 최대 30자 입니다.")
    private final String password;

    public PasswordResetRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
