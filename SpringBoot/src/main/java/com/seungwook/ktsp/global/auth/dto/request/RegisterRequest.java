package com.seungwook.ktsp.global.auth.dto.request;

import com.seungwook.ktsp.domain.user.entity.enums.AcademicYear;
import com.seungwook.ktsp.domain.user.entity.enums.Campus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class RegisterRequest {

    @Schema(description = "이메일 앞자리(@kangwon.ac.kr가 자동으로 붙음)", example = "user123")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Size(min = 2, max = 36, message = "이메일은 최소 2자 ~ 최대 36자 입니다.")
    private String email;

    @Schema(description = "사용자 비밀번호", example = "SecurePass123!")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 최소 8자 ~ 최대 30자 입니다.")
    private String password;

    @Schema(description = "사용자 학번", example = "202312345")
    @NotBlank(message = "학번은 필수 항목입니다.")
    @Size(min = 9, max = 9, message = "학번은 9자 입니다.")
    @Pattern(regexp = "^\\d{9}$", message = "학번은 숫자 9자 입니다.")
    private String studentNumber;

    @Schema(description = "사용자 이름", example = "홍길동")
    @Size(min = 2, max = 15, message = "이름은 최소 2자 ~ 최대 15자 입니다.")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(description = "캠퍼스", example = "CHUNCHEON, SAMCHEOK, DOGYE")
    @NotNull(message = "캠퍼스는 필수 항목입니다.")
    private Campus campus;

    @Schema(description = "사용자 학년", example = "FIRST_YEAR, SECOND_YEAR, THIRD_YEAR, FOURTH_YEAR, GRADUATE")
    @NotNull(message = "학년은 필수 항목입니다.")
    private AcademicYear academicYear;

    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    @NotBlank(message = "연락처 입력은 필수입니다.")
    @Pattern(regexp = "^01[0-9]-\\d{4}-\\d{4}$", message = "연락처 형식이 잘못되었습니다.")
    @Size(min = 13, max = 13, message = "연락처는 하이픈(-) 포함 13자 입니다.")
    private String phoneNumber;

    @Schema(description = "사용자 전공", example = "AI소프트웨어")
    @NotBlank(message = "전공은 필수 항목입니다.")
    @Size(min = 2, max = 20, message = "전공은 최소 2자 ~ 최대 20자 입니다.")
    private String major;

    // inclusive: 해당 값과 같아도 유효한지 여부
    @NotNull
    @Schema(description = "직전학기 학점", example = "3.66")
    @DecimalMin(value = "0.0", inclusive = true, message = "학점은 최소 0.0 이상이어야 합니다.")
    @DecimalMax(value = "4.5", inclusive = true, message = "학점은 최대 4.5 이하이어야 합니다.")
    @Digits(integer = 1, fraction = 2, message = "학점은 소수점 둘째자리까지 입력 가능합니다.")
    private BigDecimal previousGpa;
}
