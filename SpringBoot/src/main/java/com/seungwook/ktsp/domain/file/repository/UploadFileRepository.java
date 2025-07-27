package com.seungwook.ktsp.domain.file.repository;

import com.seungwook.ktsp.domain.file.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    // UUID를 바탕으로 UploadFile 리턴
    Optional<UploadFile> findByUuid(String uuid);

    // 여러 UUID를 바탕으로 List<UploadFile> 리턴
    List<UploadFile> findByUuidIn(List<String> uuids);

    // 여러 fileId를 바탕으로 UploadFile 리스트 조회
    List<UploadFile> findByIdIn(List<Long> fileIds);

}
