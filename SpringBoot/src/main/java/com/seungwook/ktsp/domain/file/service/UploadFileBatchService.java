package com.seungwook.ktsp.domain.file.service;

import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadFileBatchService {

    private final UploadFileDomainService uploadFileDomainService;
    private final FileStoreService fileStoreService;

    @Transactional
    public void deleteOldOrphanFiles() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);
        List<UploadFile> orphans = uploadFileDomainService.findOrphanUploadFies(threshold);

        for (UploadFile orphan : orphans) {
            fileStoreService.deleteFile(orphan);
            uploadFileDomainService.delete(orphan);
        }
    }

}
