package com.zeroone.ktsp.DTO;

import com.zeroone.ktsp.enumeration.UserLevel;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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