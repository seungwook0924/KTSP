package com.seungwook.ktsp.domain.board.common.comment.service;

import com.seungwook.ktsp.domain.board.common.board.service.BoardDomainService;
import com.seungwook.ktsp.domain.board.common.comment.dto.request.CommentRequest;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentDomainService commentDomainService;
    private final BoardDomainService boardDomainService;
    private final UserDomainService userDomainService;

    public void registerFreeBoardComment(long userId, CommentRequest request) {

    }
}
