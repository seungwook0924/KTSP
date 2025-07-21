package com.seungwook.ktsp.domain.board.common.handler;

import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.global.auth.support.AuthHandler;

public abstract class AccessHandler {

    // 게시글 접근 권한 확인 (관리자이거나 본인이면 true)
    public final boolean check(long boardId) {
        return hasRole() || isResourceOwner(boardId);
    }

    // 현재 사용자가 게시글의 작성자인지 여부 반환
    abstract protected boolean isResourceOwner(long boardId);

    // 현재 사용자가 지정된 권한을 보유하고 있는지 확인
    private boolean hasRole() {
        return AuthHandler.getUserRole().equals(UserRole.ADMIN);
    }
}