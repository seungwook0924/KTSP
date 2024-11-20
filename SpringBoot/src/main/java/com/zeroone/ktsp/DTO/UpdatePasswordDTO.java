package com.zeroone.ktsp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    @Size(min = 8, message = "비밀번호는 8자 이상으로 설정해주세요.")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String newPassword;

    private String confirmPassword;
}
