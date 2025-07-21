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

    public Board findById(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
