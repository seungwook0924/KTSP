package com.seungwook.ktsp.domain.file.repository;

import com.seungwook.ktsp.domain.file.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
