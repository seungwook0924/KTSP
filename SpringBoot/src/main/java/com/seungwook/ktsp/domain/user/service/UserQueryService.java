package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.exception.UserNotFoundException;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    // 내 정보 조회
    @Transactional(readOnly = true)
    public User getUserInformation(long userId) {
        return findById(userId);
    }

    // 사용자 프로필 조회
    @Transactional(readOnly = true)
    public UserProfile getUserProfile(long userId) {
        return userRepository.findUserProfileById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private User findById(long userId) {
        return userRepository.findByIdExceptWithdrawn(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
