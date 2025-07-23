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

    public List<Long> findByBoardIdIn(long boardId) {
        return boardFileRepository.findUploadFileIdsByBoardId(boardId);
    }
}
