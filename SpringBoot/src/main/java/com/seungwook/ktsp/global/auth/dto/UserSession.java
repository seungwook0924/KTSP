package com.seungwook.ktsp.global.auth.dto;

import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserSession implements Serializable {

    private final String studentNumber;
    private final String name;
    private final UserRole role;

    public UserSession(String studentNumber, String name, UserRole role) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.role = role;
    }
}
