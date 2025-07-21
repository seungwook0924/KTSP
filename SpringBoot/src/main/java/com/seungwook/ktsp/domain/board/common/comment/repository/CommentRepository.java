package com.seungwook.ktsp.domain.board.common.comment.repository;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // board 타입의 전체 부모 댓글을 반환
    List<Comment> findByBoardAndParentCommentIsNull(Board board);

    // commentId를 바탕으로 댓글 작성자의 userId를 리턴(AccessHandler 최적화)
    @Query("SELECT c.user.id FROM Comment c WHERE c.id = :commentId")
    Long findWriterIdById(Long commentId);

}
