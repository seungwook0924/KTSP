package com.seungwook.ktsp.domain.comment.repository;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.comment.entity.Comment;
import com.seungwook.ktsp.domain.comment.repository.querydsl.CommentQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {

    // commentId를 바탕으로 댓글 작성자의 userId를 리턴(AccessHandler 최적화)
    @Query("SELECT c.user.id FROM Comment c WHERE c.id = :commentId")
    Optional<Long> findWriterIdById(Long commentId);

    // 특정 게시글의 모든 댓글 삭제
    void deleteAllByBoard(Board board);
}
