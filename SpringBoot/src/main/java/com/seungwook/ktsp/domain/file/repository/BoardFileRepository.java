package com.seungwook.ktsp.domain.file.repository;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.file.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

    // boardId를 기준으로 UploadFile의 id만 가져옴
    @Query("SELECT bf.file.id FROM BoardFile bf WHERE bf.board.id = :boardId")
    List<Long> findUploadFileIdsByBoardId(long boardId);

    List<BoardFile> findByBoard(Board board);
}
