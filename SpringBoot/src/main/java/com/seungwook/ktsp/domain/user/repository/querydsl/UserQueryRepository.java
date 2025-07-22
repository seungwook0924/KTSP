package com.seungwook.ktsp.domain.user.repository.querydsl;

import com.seungwook.ktsp.domain.user.dto.UserProfile;

import java.util.Optional;

public interface UserQueryRepository {

    // userId를 바탕으로 User 프로필 조회
    Optional<UserProfile> findUserProfileById(Long userId);
}

