package com.seungwook.ktsp.domain.board.common.comment.repository;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // board 타입의 전체 부모 댓글을 반환
    List<Comment> findByBoardAndParentCommentIsNull(Board board);

    // 댓글 작성자 ID만 조회 (AccessHandler 최적화)
    @Query("SELECT c.user.id FROM Comment c WHERE c.id = :id")
    Long findWriterIdById(@Param("id") Long commentId);

}
