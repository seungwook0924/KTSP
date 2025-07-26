package com.seungwook.ktsp.domain.user.repository.querydsl;

import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.dto.WriterInfo;

import java.util.Optional;

public interface UserQueryRepository {

    // userId를 바탕으로 User 프로필 조회
    Optional<UserProfile> findUserProfileById(Long userId);

    // userId를 바탕으로 WriterInfo 리턴(게시글 작성시 보여질 정보)
    Optional<WriterInfo> findWriterInfoById(Long userId);
}

