package com.seungwook.ktsp.domain.file.repository;

import com.seungwook.ktsp.domain.file.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
