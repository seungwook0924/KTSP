package com.seungwook.ktsp.domain.board.common.comment.service;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.board.common.comment.repository.CommentRepository;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> findParentComment(Board board) {
        return commentRepository.findByBoardAndParentCommentIsNull(board);
    }

    // freeBoard의 부모 댓글 반환
    public List<Comment> findByFreeBoardAndParentIsNull(Notice notice) {
        return commentRepository.findByBoardAndParentIsNull(notice);
    }
}
