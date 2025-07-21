package com.seungwook.ktsp.domain.board.common.comment.service;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.board.common.comment.exception.CommentNotFoundException;
import com.seungwook.ktsp.domain.board.common.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;

    // 댓글 생성
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    // 해당 게시글의 모든 부모 댓글 반환
    public List<Comment> findParentComment(Board board) {
        return commentRepository.findByBoardAndParentCommentIsNull(board);
    }

    public Comment findById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
    }
}
