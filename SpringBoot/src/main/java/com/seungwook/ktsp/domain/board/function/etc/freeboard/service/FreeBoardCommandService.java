package com.seungwook.ktsp.domain.board.function.etc.freeboard.service;

import com.seungwook.ktsp.domain.board.common.service.BoardDomainService;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.RegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.freeboard.entity.FreeBoard;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreeBoardCommandService {

    private final BoardDomainService boardDomainService;
    private final UserDomainService userDomainService;

    @Transactional
    public void freeBoardRegister(long userId, RegisterRequest request) {

        User user = userDomainService.findActiveUserById(userId);

        FreeBoard freeBoard = FreeBoard.createFreeBoard(user, request.getTitle(), request.getContent());

        boardDomainService.save(freeBoard);
    }
}
