package com.seungwook.ktsp.domain.file.service.domain;

import com.seungwook.ktsp.domain.file.entity.BoardFile;
import com.seungwook.ktsp.domain.file.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFileDomainService {

    private final BoardFileRepository boardFileRepository;

    public void save(BoardFile boardFile) {
        boardFileRepository.save(boardFile);
    }

    // 게시글에 연결된 모든 fileId(UploadFile PK)를 리턴
    public List<Long> findByBoardIdIn(long boardId) {
        return boardFileRepository.findUploadFileIdsByBoardId(boardId);
    }
}
