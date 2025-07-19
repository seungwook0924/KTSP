package com.seungwook.ktsp.domain.user.repository;

import com.seungwook.ktsp.domain.user.dto.UserProfile;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserQueryRepository {

    // userId를 바탕으로 User 프로필 조회
    Optional<UserProfile> findUserProfileById(@Param("id") Long userId);
}

