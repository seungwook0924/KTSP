package com.seungwook.ktsp.domain.comment.repository;

import com.seungwook.ktsp.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // boardId를 바탕으로 전체 부모 댓글을 반환
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.parent IS NULL")
    List<Comment> findParentCommentsByBoardId(Long boardId);


    // commentId를 바탕으로 댓글 작성자의 userId를 리턴(AccessHandler 최적화)
    @Query("SELECT c.user.id FROM Comment c WHERE c.id = :commentId")
    Optional<Long> findWriterIdById(Long commentId);

}
