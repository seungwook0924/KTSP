package com.seungwook.ktsp.domain.file.service.domain;

import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.exception.FileNotFoundException;
import com.seungwook.ktsp.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadFileDomainService {

    private final UploadFileRepository uploadFileRepository;

    // UploadFile 저장
    @Transactional
    public void save(UploadFile file) {
        uploadFileRepository.save(file);
    }

    // uuid를 바탕으로 UploadFile 리턴
    @Transactional(readOnly = true)
    public UploadFile findByUuid(String uuid) {
        return uploadFileRepository.findByUuid(uuid)
                .orElseThrow(FileNotFoundException::new);
    }

    // 여러 UploadFile 조회
    @Transactional(readOnly = true)
    public List<UploadFile> findByUuidIn(List<String> uuids) {
        return uploadFileRepository.findByUuidIn(uuids);
    }

    // UploadFile의 PK를 바탕으로 여러 UploadFile 조회
    @Transactional(readOnly = true)
    public List<UploadFile> findByIdIn(List<Long> fileIds) {
        return uploadFileRepository.findByIdIn(fileIds);
    }

    // UploadFile 삭제
    @Transactional
    public void delete(UploadFile uploadFile) {
        uploadFileRepository.delete(uploadFile);
    }

    // 지정된 시간 이전에 생성되고 BoardFile에 연결되지 않은 UploadFile 목록 조회
    @Transactional(readOnly = true)
    public List<UploadFile> findOrphanUploadFiles(LocalDateTime threshold) {
        return uploadFileRepository.findOrphanUploadFiles(threshold);
    }
}
