package com.seungwook.ktsp.domain.comment.service;

import com.seungwook.ktsp.domain.comment.entity.Comment;
import com.seungwook.ktsp.domain.comment.exception.CommentNotFoundException;
import com.seungwook.ktsp.domain.comment.repository.CommentRepository;
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
    public List<Comment> findParentComment(long boardId) {
        return commentRepository.findParentCommentsByBoardId(boardId);
    }

    // 댓글 작성자 userId 리턴
    public Long findWriterIdById(long commentId) {
        return commentRepository.findWriterIdById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    // 프록시 객체 반환(성능 최적화)
    public Comment getReferenceById(long commentId) {
        if(!commentRepository.existsById(commentId))
            throw new CommentNotFoundException();

        return commentRepository.getReferenceById(commentId);
    }

    public Comment findById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
}
