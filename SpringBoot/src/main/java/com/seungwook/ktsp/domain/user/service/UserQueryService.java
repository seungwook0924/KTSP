package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.mapper.UserMapper;
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
        return userDomainService.findActiveUserById(userId);
    }

    // 사용자 프로필 조회
    @Transactional(readOnly = true)
    public UserProfile getUserProfile(long userId) {
        return userDomainService.findUserProfileById(userId);
    }

    // 게시글 작성자 정보 조회
    @Transactional(readOnly = true)
    public Writer getWriter(long userId) {
        return UserMapper.toWriter(userDomainService.findWriterInfoById(userId));
    }
}
