package com.seungwook.ktsp.global.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "학번은 필수 항목입니다.")
    @Size(min = 9, max = 9, message = "학번은 9자 입니다.")
    private final String studentNumber;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 60, message = "비밀번호는 최소 8자 ~ 최대 60자 입니다.")
    private final String password;

    public LoginRequest(String studentNumber, String password) {
        this.studentNumber = studentNumber;
        this.password = password;
    }
}
