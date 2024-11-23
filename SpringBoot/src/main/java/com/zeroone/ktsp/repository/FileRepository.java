package com.zeroone.ktsp.repository;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.FileMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileMapping, Long>
{
    // board_id로 파일 목록 조회
    List<FileMapping> findByBoardId(long boardId);

    // board 객체를 직접 사용해 파일 목록 조회
    List<FileMapping> findByBoard(Board board);

    Optional<FileMapping> findByBoardAndFileName(Board board, String fileName);
}
