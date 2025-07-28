package com.seungwook.ktsp.domain.comment.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.board.common.service.BoardDomainService;
import com.seungwook.ktsp.domain.comment.dto.request.CommentReplyRequest;
import com.seungwook.ktsp.domain.comment.dto.request.CommentRequest;
import com.seungwook.ktsp.domain.comment.dto.request.CommentUpdateRequest;
import com.seungwook.ktsp.domain.comment.entity.Comment;
import com.seungwook.ktsp.domain.comment.service.domain.CommentDomainService;
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

        // User 프록시
        User user = userDomainService.getReferenceById(userId);

        // Board 프록시
        Board board = boardDomainService.getReferenceById(request.getBoardId());

        // 댓글 생성 및 저장
        Comment comment = Comment.createComment(user, board, request.getComment());
        commentDomainService.save(comment);
    }

    // 대댓글 등록
    @Transactional
    public void registerReply(long userId, CommentReplyRequest request) {

        // User 프록시
        User user = userDomainService.getReferenceById(userId);

        // 부모 댓글 프록시
        Comment parentComment = commentDomainService.getReferenceById(request.getParentCommentId());

        // 댓글 생성 및 저장
        Comment comment = Comment.createReply(user, parentComment, request.getComment());
        commentDomainService.save(comment);
    }

    // 댓글 수정
    @Transactional
    @PreAuthorize("@commentAccessHandler.check(#request.getCommentId())")
    public void updateComment(CommentUpdateRequest request) {

        // Comment 프록시
        Comment comment = commentDomainService.getReferenceById(request.getCommentId());

        // 댓글 수정
        comment.updateComment(request.getComment());
    }

    // 댓글 삭제
    @Transactional
    @PreAuthorize("@commentAccessHandler.check(#commentId)")
    public void deleteComment(long commentId) {

        // Comment 프록시
        Comment comment = commentDomainService.getReferenceById(commentId);

        // 댓글 삭제
        commentDomainService.delete(comment);
    }
}
