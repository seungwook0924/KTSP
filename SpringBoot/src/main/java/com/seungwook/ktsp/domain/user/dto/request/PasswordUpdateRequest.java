package com.seungwook.ktsp.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordUpdateRequest {

    @Schema(description = "기존 비밀번호", example = "oldPassword123!")
    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 30, message = "비밀번호는 최소 8자 ~ 최대 30자 입니다.")
    private final String oldPassword;

    @Schema(description = "신규 비밀번호", example = "newPassword123!")
    @NotBlank(message = "변경할 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 30, message = "비밀번호는 최소 8자 ~ 최대 30자 입니다.")
    private final String newPassword;
}
