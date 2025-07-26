package com.seungwook.ktsp.domain.board.common.handler;

import com.seungwook.ktsp.domain.board.common.service.BoardDomainService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.handler.AccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAccessHandler extends AccessHandler {

    private final BoardDomainService boardDomainService;

    // 현재 사용자가 게시글의 작성자인지 여부 반환
    @Override
    protected boolean isResourceOwner(long boardId) {
        long writerId = boardDomainService.findWriterIdById(boardId); // 게시글 작성자
        return writerId == AuthHandler.getUserId(); // 게시글 작성자와 현재 사용자가 같은지 여부
    }
}
