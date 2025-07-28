package com.seungwook.ktsp.global.handler;

import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.global.auth.support.AuthHandler;

public abstract class AccessHandler {

    // 접근 권한 확인 (관리자 또는 작성자)
    public final boolean check(long Id) {
        return isAdmin() || isResourceOwner(Id);
    }

    // 리소스 소유자인지 확인 (하위 구현체에서 정의)
    abstract protected boolean isResourceOwner(long Id);

    // 관리자 권한 여부
    private boolean isAdmin() {
        return AuthHandler.getUserRole() == UserRole.ADMIN;
    }
}
