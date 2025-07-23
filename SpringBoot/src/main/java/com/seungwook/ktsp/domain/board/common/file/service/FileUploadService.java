package com.seungwook.ktsp.domain.board.common.file.service;

import com.seungwook.ktsp.domain.board.common.file.entity.UploadFile;
import com.seungwook.ktsp.domain.board.common.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileStoreService fileStoreService;
    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public String uploadFile(MultipartFile file, boolean isImageFile) {

        UploadFile uploadFile = fileStoreService.storeFile(file, isImageFile);

        uploadFileRepository.save(uploadFile);

        return fileStoreService.getFileAccessPath(uploadFile);
    }
}
