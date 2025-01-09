package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Comment;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService
{
    private final CommentRepository commentRepository;

    public Optional<Comment> findById(long id)
    {
        return commentRepository.findById(id);
    }

    public List<Comment> findByBoard(Board board)
    {
        return commentRepository.findByBoard(board);
    }

    // 댓글만 반환 (부모 댓글이 없는 댓글)
    public List<Comment> findCommentsByBoard(Board board)
    {
        return commentRepository.findByBoardAndParentCommentIsNull(board);
    }

    public void save(String content, User user, Board board)
    {
        Comment newComment = Comment.builder()
                .comment(content)
                .user(user)
                .board(board)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(newComment);
    }

    public void saveReply(String content, User user, Board board, Comment parentComment)
    {
        Comment replyComment = Comment.builder()
                .comment(content)
                .user(user)
                .board(board)
                .parentComment(parentComment) // 부모 댓글 설정
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(replyComment);
    }

    public void deleteComment(Comment comment)
    {
        commentRepository.delete(comment);
    }
}
