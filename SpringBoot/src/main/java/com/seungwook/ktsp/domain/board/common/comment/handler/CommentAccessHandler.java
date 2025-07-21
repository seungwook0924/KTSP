package com.seungwook.ktsp.domain.board.common.comment.handler;

import com.seungwook.ktsp.global.handler.AccessHandler;
import com.seungwook.ktsp.domain.board.common.comment.service.CommentDomainService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAccessHandler extends AccessHandler {

    private final CommentDomainService commentDomainService;

    @Override
    protected boolean isResourceOwner(long commentId) {
        long writerId = commentDomainService.findById(commentId).getUser().getId();
        long currentUserId = AuthHandler.getUserId();
        return writerId == currentUserId;
    }
}
