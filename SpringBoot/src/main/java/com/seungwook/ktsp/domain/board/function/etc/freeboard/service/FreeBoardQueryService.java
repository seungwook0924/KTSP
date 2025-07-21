package com.seungwook.ktsp.domain.board.function.etc.freeboard.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.board.common.service.BoardDomainService;
import com.seungwook.ktsp.domain.board.function.etc.freeboard.entity.FreeBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreeBoardQueryService {

    private final BoardDomainService boardDomainService;

    @Transactional(readOnly = true)
    public FreeBoard getFreeBoard(long boardId) {
        Board board = boardDomainService.findById(boardId);

        return (FreeBoard) board;
    }
}
