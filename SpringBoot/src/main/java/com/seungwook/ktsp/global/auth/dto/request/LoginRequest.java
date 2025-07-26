package com.seungwook.ktsp.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "사용자 학번", example = "202312345")
    @NotBlank(message = "학번은 필수 항목입니다.")
    @Size(min = 9, max = 9, message = "학번은 9자 입니다.")
    @Pattern(regexp = "^\\d{9}$", message = "학번은 숫자 9자 입니다.")
    private final String studentNumber;

    @Schema(description = "사용자 비밀번호", example = "SecurePass123!")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 최소 8자 ~ 최대 30자 입니다.")
    private final String password;

    @Schema(description = "자동 로그인 여부")
    private final boolean isRememberMe;
}
