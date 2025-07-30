package com.seungwook.ktsp.domain.file.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.seungwook.ktsp.domain.file.entity.QBoardFile.boardFile;
import static com.seungwook.ktsp.domain.file.entity.QUploadFile.uploadFile;

@Repository
@RequiredArgsConstructor
public class UploadFileQueryRepositoryImpl implements UploadFileQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UploadFile> findOrphanUploadFiles(LocalDateTime threshold) {
        return queryFactory
                .selectFrom(uploadFile)
                .leftJoin(boardFile).on(boardFile.file.eq(uploadFile))
                .where(uploadFile.createdAt.lt(threshold)
                        .and(boardFile.id.isNull()))
                .orderBy(uploadFile.id.asc())
                .limit(500)
                .fetch();
    }
}