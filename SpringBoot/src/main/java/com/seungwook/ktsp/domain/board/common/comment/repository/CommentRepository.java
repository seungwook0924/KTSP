package com.seungwook.ktsp.domain.board.common.comment.repository;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // board 타입의 전체 부모 댓글을 반환
    List<Comment> findByBoardAndParentCommentIsNull(Board board);

    // freeboard 타입의 전체 부모 댓글을 반환
    List<Comment> findByBoardAndParentIsNull(Notice notice);

}
