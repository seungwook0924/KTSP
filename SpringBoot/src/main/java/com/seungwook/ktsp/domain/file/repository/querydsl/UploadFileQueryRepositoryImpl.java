package com.seungwook.ktsp.domain.file.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.seungwook.ktsp.domain.file.entity.QBoardFile.boardFile;
import static com.seungwook.ktsp.domain.file.entity.QUploadFile.uploadFile;

@Repository
@RequiredArgsConstructor
public class UploadFileQueryRepositoryImpl implements UploadFileQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UploadFile> findOrphanUploadFies(LocalDateTime threshold) {
        return queryFactory
                .selectFrom(uploadFile)
                .where(
                        uploadFile.createdAt.lt(threshold)
                        .and(uploadFile.id.notIn(select(boardFile.file.id).from(boardFile)))
                )
                .fetch();
    }
}