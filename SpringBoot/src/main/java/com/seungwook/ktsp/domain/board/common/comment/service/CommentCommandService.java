package com.seungwook.ktsp.domain.board.common.comment.service;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.board.service.BoardDomainService;
import com.seungwook.ktsp.domain.board.common.comment.dto.request.CommentRequest;
import com.seungwook.ktsp.domain.board.common.comment.dto.request.CommentUpdateRequest;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentDomainService commentDomainService;
    private final BoardDomainService boardDomainService;
    private final UserDomainService userDomainService;

    // 댓글 등록
    @Transactional
    public void registerComment(long userId, CommentRequest request) {
        User user = userDomainService.findActiveUserById(userId);
        Board board = boardDomainService.getReferenceById(request.getBoardId());
        Comment comment = Comment.createComment(user, board, request.getComment());
        commentDomainService.save(comment);
    }

    // 댓글 수정
    @Transactional
    @PreAuthorize("@commentAccessHandler.check(#commentId)")
    public void updateComment(long commentId, CommentUpdateRequest request) {
        Comment comment = commentDomainService.getReferenceById(commentId);
        comment.updateComment(request.getComment());
    }

    // 댓글 삭제
    @Transactional
    @PreAuthorize("@commentAccessHandler.check(#commentId)")
    public void deleteComment(long commentId) {
        Comment comment = commentDomainService.getReferenceById(commentId);
        commentDomainService.delete(comment);
    }
}
