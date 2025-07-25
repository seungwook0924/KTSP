package com.seungwook.ktsp.domain.file.service;

import com.seungwook.ktsp.domain.file.dto.AttachedFile;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.BoardFileDomainService;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 파일 업로드
    @Transactional
    public String uploadFile(MultipartFile file, boolean isImageFile) {

        UploadFile uploadFile = fileStoreService.storeFile(file, isImageFile);

        uploadFileDomainService.save(uploadFile);

        String accessUrlPrefix = fileStoreService.getAccessUrlPrefix();
        String uuid = uploadFile.getUuid();
        String type = uploadFile.getType();

        // 이미지 파일이라면
        if (isImageExtension(uploadFile.getType()))
            return accessUrlPrefix + uuid + ensureDotPrefix(type);

        // 일반 첨부파일이라면 uuid만 리턴
        return uuid;
    }

    // 첨부파일 다운로드 주소 리턴
    public List<AttachedFileInfo> getAttachedFileDownloadPath(long boardId) {

        List<Long> fileIds = boardFileDomainService.findByBoardIdIn(boardId);
        List<UploadFile> uploadFiles = uploadFileDomainService.findByIdIn(fileIds);

        List<AttachedFileInfo> response = new ArrayList<>();

        for (UploadFile file : uploadFiles) {
            response.add(fileStoreService.getAttachedFileInfo(file));
        }

        return response;
    }

    // 첨부파일 다운로드
    public AttachedFile downloadFile(String uuid) {
        UploadFile uploadFile = uploadFileDomainService.findByUuid(uuid);
        return fileStoreService.downloadFile(uploadFile);
    }
}
