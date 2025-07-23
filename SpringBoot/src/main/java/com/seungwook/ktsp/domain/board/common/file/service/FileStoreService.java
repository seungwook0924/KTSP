package com.seungwook.ktsp.domain.board.common.file.service;

import com.seungwook.ktsp.domain.board.common.file.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileStoreService {

    // 파일 저장
    UploadFile storeFile(MultipartFile file, boolean isImageFile);

    // 파일 삭제
    void deleteFile(UploadFile uploadFile);

    // 파일 접근가능 경로 리턴
    String getFileAccessPath(UploadFile uploadFile);
}
