package com.zeroone.ktsp.DTO.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String studentNumber;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
