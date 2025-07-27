package com.seungwook.ktsp.domain.board.type.community.notice.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.seungwook.ktsp.domain.board.type.community.notice.entity.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepositoryImpl implements NoticeQueryRepository{

    private final JPAQueryFactory queryFactory;

    // 모든 공지사항을 조회(페이징)
    @Override
    public Page<CommunityList> findAllNoticePage(Pageable pageable) {
        List<CommunityList> content = queryFactory
                .select(Projections.constructor(
                        CommunityList.class,
                        notice.id,
                        notice.title,
                        notice.hits,
                        notice.createdAt)
                )
                .from(notice)
                .orderBy(notice.id.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(notice.count())
                        .from(notice)
                        .fetchOne()
                ).orElse(0L);


        return new PageImpl<>(content, pageable, total);
    }
}
