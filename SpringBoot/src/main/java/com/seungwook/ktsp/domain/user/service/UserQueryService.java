package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserDomainService userDomainService;

    // 내 정보 조회
    @Transactional(readOnly = true)
    public User getUserInformation(long userId) {
        return findById(userId);
    }

    // 사용자 프로필 조회
    @Transactional(readOnly = true)
    public UserProfile getUserProfile(long userId) {
        return userDomainService.findUserProfileById(userId);
    }

    // 활성화된 회원 조회
    private User findById(long userId) {
        return userDomainService.findActiveUserById(userId);
    }
}
