package com.seungwook.ktsp.domain.file.service;

import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadFileBatchService {

    private final UploadFileDomainService uploadFileDomainService;
    private final FileStoreService fileStoreService;

    public void deleteOldOrphanFiles() {
        // 기준 시각: 하루 전
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);

        long total = 0, success = 0, fail = 0;

        while (true) {
            // 하루 이상 지난 고아 파일 500건씩 조회 (없는 경우 종료)
            List<UploadFile> orphans = uploadFileDomainService.findOrphanUploadFiles(threshold);
            if (orphans.isEmpty()) break;

            for (UploadFile file : orphans) {

                total++;

                try {
                    // 스토리지에서 실제 파일 삭제
                    fileStoreService.deleteFile(file);

                    // DB에서 파일 메타데이터 삭제
                    uploadFileDomainService.delete(file);
                    success++;
                } catch (Exception e) {
                    fail++;
                    log.warn("고아 파일 삭제 실패 - id={}, uuid={}", file.getId(), file.getUuid(), e);
                }
            }
        }

        log.info("total Access={} / success={} / fail={}", total, success, fail);
    }
}
