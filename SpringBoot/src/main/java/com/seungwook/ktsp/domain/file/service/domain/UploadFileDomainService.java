package com.seungwook.ktsp.domain.file.service.domain;

import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.exception.FileNotFoundException;
import com.seungwook.ktsp.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadFileDomainService {

    private final UploadFileRepository uploadFileRepository;

    public void save(UploadFile file) {
        uploadFileRepository.save(file);
    }

    public UploadFile findByUuid(String uuid) {
        return uploadFileRepository.findByUuid(uuid)
                .orElseThrow(FileNotFoundException::new);
    }

    public List<UploadFile> findByUuidIn(List<String> uuids) {
        return uploadFileRepository.findByUuidIn(uuids);
    }

    public List<UploadFile> findByIdIn(List<Long> fileIds) {
        return uploadFileRepository.findByIdIn(fileIds);
    }
}
