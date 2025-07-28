package com.seungwook.ktsp.domain.board.type.community.report.repository.querydsl;

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

import static com.seungwook.ktsp.domain.board.type.community.report.entity.QReport.report;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepositoryImpl implements ReportQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommunityList> getUserReports(Long userId, Pageable pageable) {

        // 본인이 작성한 Report 리스트 페이징 조회
        List<CommunityList> contents = queryFactory
                .select(Projections.constructor(
                        CommunityList.class,
                        report.id,
                        report.title,
                        report.hits,
                        report.user.name,
                        report.createdAt
                ))
                .from(report)
                .where(report.user.id.eq(userId))
                .orderBy(report.id.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        Long total = Optional.ofNullable(
                queryFactory
                        .select(report.count())
                        .from(report)
                        .where(report.user.id.eq(userId))
                        .fetchOne()
        ).orElse(0L);


        return new PageImpl<>(contents, pageable, total);
    }
}
