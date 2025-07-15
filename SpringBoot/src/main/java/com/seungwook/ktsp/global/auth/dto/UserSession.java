package com.seungwook.ktsp.global.auth.dto;

import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/*
    Spring Security 세션에 저장되는 인증 사용자 정보 객체.
    HttpSession의 SPRING_SECURITY_CONTEXT 키에 저장되는 SecurityContext 내 Authentication의 principal로 사용됨.
    - 직렬화를 위해 Serializable 구현
    - 사용자 식별을 위한 최소한의 정보만 포함
    - UserSession 구조 변경시 serialVersionUID 값을 올릴 것
*/
@Getter
public class UserSession implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String studentNumber;
    private final String name;
    private final UserRole role;

    public UserSession(String studentNumber, String name, UserRole role) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.role = role;
    }
}
