package com.seungwook.ktsp.domain.file.service;

import com.seungwook.ktsp.domain.file.dto.AttachedFile;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.exception.FileException;
import com.seungwook.ktsp.domain.file.service.domain.BoardFileDomainService;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.seungwook.ktsp.domain.file.utils.FileNameUtils.ensureDotPrefix;
import static com.seungwook.ktsp.domain.file.utils.FileNameUtils.isImageExtension;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStoreService fileStoreService;
    private final BoardFileDomainService boardFileDomainService;
    private final UploadFileDomainService uploadFileDomainService;
    private final UserDomainService userDomainService;

    // 파일 업로드
    @Transactional
    public String uploadFile(long userId, MultipartFile file, boolean isImageFile) {

        // 프록시 객체
        User user = userDomainService.getReferenceById(userId);

        // 파일 저장
        UploadFile uploadFile = fileStoreService.storeFile(user, file, isImageFile);
        uploadFileDomainService.save(uploadFile);

        // 파일 접근 경로 리턴
        String accessUrlPrefix = fileStoreService.getAccessUrlPrefix();
        String uuid = uploadFile.getUuid();
        String extension = uploadFile.getExtension();

        // 이미지 파일이라면 전체 접근 경로 리턴
        if (isImageExtension(uploadFile.getExtension()))
            return accessUrlPrefix + uuid + ensureDotPrefix(extension);

        // 일반 첨부파일이라면 uuid만 리턴
        return uuid;
    }

    // 첨부파일 다운로드 주소 리턴
    public List<AttachedFileInfo> getAttachedFileDownloadPath(long boardId) {

        // 게시글에 연결된 모든 fileId(UploadFile PK)를 리턴
        List<Long> fileIds = boardFileDomainService.findByBoardIdIn(boardId);

        // fileId를 바탕으로 모든 UploadFile을 조회
        List<UploadFile> uploadFiles = uploadFileDomainService.findByIdIn(fileIds);

        // 첨부파일 정보 리턴(파일 이름, 다운로드 경로, 확장자)
        List<AttachedFileInfo> response = new ArrayList<>();
        for (UploadFile file : uploadFiles)
            response.add(fileStoreService.getAttachedFileInfo(file));

        return response;
    }

    // 첨부파일 다운로드
    public AttachedFile downloadFile(String uuid) {

        UploadFile uploadFile = uploadFileDomainService.findByUuid(uuid);

        return fileStoreService.downloadFile(uploadFile);
    }

    // 파일 삭제
    @Transactional
    public void deleteFile(String uuid, long requestUserId, UserRole requestUserRole) {

        UploadFile uploadFile = uploadFileDomainService.findByUuid(uuid);

        // 파일 삭제 권한이 있는지 검사
        boolean isNotResourceOwner = uploadFile.getUser().getId() != requestUserId;
        boolean isNotAdmin = !requestUserRole.equals(UserRole.ADMIN);
        if (isNotResourceOwner && isNotAdmin)
            throw new FileException(HttpStatus.FORBIDDEN, "파일을 삭제할 권한이 없습니다.");

        // DB에서 제거
        uploadFileDomainService.delete(uploadFile);

        // 스토리지에서 삭제
        fileStoreService.deleteFile(uploadFile);
    }
}
