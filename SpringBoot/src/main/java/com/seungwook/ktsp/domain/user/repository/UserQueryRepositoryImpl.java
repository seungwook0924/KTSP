package com.seungwook.ktsp.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.Projections;

import java.util.Optional;


@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserProfile> findUserProfileById(Long userId) {
        QUser user = QUser.user;

        return Optional.ofNullable(
                queryFactory
                        .select(
                                Projections.constructor(
                                        UserProfile.class,
                                        user.name,
                                        user.major,
                                        user.studentNumber,
                                        user.introduction,
                                        user.status
                                )
                        )
                        .from(user)
                        .where(user.id.eq(userId))
                        .fetchOne()
        );
    }
}