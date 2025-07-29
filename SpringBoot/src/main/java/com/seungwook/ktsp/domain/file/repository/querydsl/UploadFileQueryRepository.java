package com.seungwook.ktsp.domain.file.repository.querydsl;

import com.seungwook.ktsp.domain.file.entity.UploadFile;

import java.time.LocalDateTime;
import java.util.List;

public interface UploadFileQueryRepository {

    // 지정된 시간 이전에 생성되고 BoardFile에 연결되지 않은 UploadFile 목록 조회
    List<UploadFile> findOrphanUploadFies(LocalDateTime threshold);
}
