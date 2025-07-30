package com.seungwook.ktsp.domain.file.support;

import com.seungwook.ktsp.domain.file.service.UploadFileBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadFileCleanerScheduler {

    private final UploadFileBatchService uploadFileBatchService;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void cleanOrphanFiles() {
        log.info("고아 파일 삭제 시작");
        uploadFileBatchService.deleteOldOrphanFiles();
        log.info("고아 파일 삭제 종료");
    }
}
