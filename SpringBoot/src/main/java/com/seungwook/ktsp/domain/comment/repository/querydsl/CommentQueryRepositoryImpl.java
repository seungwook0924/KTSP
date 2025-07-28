package com.seungwook.ktsp.domain.comment.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungwook.ktsp.domain.comment.dto.CommentInfo;
import com.seungwook.ktsp.domain.comment.entity.QComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentInfo> getCommentsByBoardId(Long boardId) {

        QComment comment = QComment.comment1;

        return queryFactory
                .select(Projections.constructor(
                        CommentInfo.class,
                        comment.user.id,
                        comment.user.name,
                        comment.id,
                        comment.parent.id,
                        comment.comment,
                        comment.createdAt,
                        comment.modifiedAt
                ))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdAt.desc(), comment.id.desc()) // 최신순
                .fetch();
    }
}
