package com.zeroone.ktsp.DTO;

import com.zeroone.ktsp.enumeration.UserLevel;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateUserDTO {

    @NotNull(message = "학년은 필수 항목입니다.")
    private UserLevel level;

    @NotBlank(message = "연락처 입력은 필수입니다.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "연락처가 잘못되었습니다.")
    private String tel;

    @NotBlank(message = "전공 입력은 필수입니다.")
    @Size(min = 2, max = 20, message = "전공의 길이는 2 ~ 20 글자입니다.")
    private String major;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "학점은 최소 0.0 이상이어야 합니다.")
    @DecimalMax(value = "4.5", inclusive = true, message = "학점은 최대 4.5 이하이어야 합니다.")
    private BigDecimal lastGrades;
}