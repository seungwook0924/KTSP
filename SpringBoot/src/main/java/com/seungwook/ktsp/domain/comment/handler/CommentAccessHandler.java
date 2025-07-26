package com.seungwook.ktsp.domain.comment.handler;

import com.seungwook.ktsp.global.handler.AccessHandler;
import com.seungwook.ktsp.domain.comment.service.CommentDomainService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAccessHandler extends AccessHandler {

    private final CommentDomainService commentDomainService;

    // 현재 사용자가 댓글의 작성자인지 여부 반환
    @Override
    protected boolean isResourceOwner(long commentId) {
        long writerId = commentDomainService.findWriterIdById(commentId); // 댓글 작성자
        return writerId == AuthHandler.getUserId(); // 댓글 작성자와 현재 사용자가 같은지 여부
    }
}
