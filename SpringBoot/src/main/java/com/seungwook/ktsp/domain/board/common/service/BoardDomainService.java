package com.seungwook.ktsp.domain.board.common.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.board.common.exception.BoardNotFoundException;
import com.seungwook.ktsp.domain.board.common.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardDomainService {

    private final BoardRepository boardRepository;

    // 게시판 저장
    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    // 게시글 작성자 userId 리턴
    @Transactional(readOnly = true)
    public long findWriterIdById(long boardId) {
        return boardRepository.findWriterIdById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }

    // 프록시 객체 반환(성능 최적화)
    public Board getReferenceById(long boardId) {
        if (!boardRepository.existsById(boardId))
            throw new BoardNotFoundException();

        return boardRepository.getReferenceById(boardId);
    }

    @Transactional(readOnly = true)
    public Board findById(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
