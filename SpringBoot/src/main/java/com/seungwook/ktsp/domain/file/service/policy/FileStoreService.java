package com.seungwook.ktsp.domain.file.service.policy;

import com.seungwook.ktsp.domain.file.dto.AttachedFile;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface FileStoreService {

    // 파일 저장
    UploadFile storeFile(User user, MultipartFile file, boolean isImageFile);

    // 파일 삭제
    void deleteFile(UploadFile uploadFile);

    // 파일 다운로드
    AttachedFile downloadFile(UploadFile uploadFile);

    // 파일 접근 가능 URL 접두사 리턴
    String getAccessUrlPrefix();

    // 첨부파일 정보 리턴
    AttachedFileInfo getAttachedFileInfo(UploadFile uploadFile);
}
