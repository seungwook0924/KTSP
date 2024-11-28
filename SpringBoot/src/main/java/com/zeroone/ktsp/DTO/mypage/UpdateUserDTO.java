package com.zeroone.ktsp.DTO.mypage;

import com.zeroone.ktsp.enumeration.UserLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private UserLevel level;
    private String tel;
    private String major;
    private String lastGrades;

    private String newPassword;
    private String confirmPassword;
}