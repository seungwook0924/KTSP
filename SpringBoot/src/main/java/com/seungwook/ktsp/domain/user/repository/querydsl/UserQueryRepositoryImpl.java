package com.seungwook.ktsp.domain.user.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.dto.WriterInfo;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.seungwook.ktsp.domain.user.entity.QUser.*;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    // userId를 바탕으로 User 프로필 조회
    @Override
    public Optional<UserProfile> findUserProfileById(Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(UserProfile.class,
                                user.name,
                                user.major,
                                user.studentNumber,
                                user.introduction,
                                user.status
                        ))
                        .from(user)
                        .where(user.id.eq(userId))
                        .fetchOne()
        );
    }

    // userId를 바탕으로 WriterInfo 리턴(게시글 작성시 보여질 정보)
    @Override
    public Optional<WriterInfo> findWriterInfoById(Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(WriterInfo.class,
                                user.id,
                                user.name,
                                user.major,
                                user.studentNumber,
                                user.status
                        ))
                        .from(user)
                        .where(user.id.eq(userId))
                        .fetchOne()
        );
    }
}