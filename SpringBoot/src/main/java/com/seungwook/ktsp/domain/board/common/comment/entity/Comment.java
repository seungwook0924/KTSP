package com.seungwook.ktsp.domain.board.common.comment.entity;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.comment.exception.CommentException;
import com.seungwook.ktsp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Comment와 User는 N:1 관계
    // 여러 댓글은 하나의 유저에 의해 작성될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // Comment와 Board는 N:1 관계
    // 여러개의 댓글은 하나의 게시글에 달릴 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    private Board board;

    // 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // null이면 일반 댓글, not null이면 대댓글

    // 댓글 내용
    @Column(nullable = false, length = 100)
    private String comment;

    // 생성자
    private Comment(User user, Board board, String comment, Comment parent) {
        this.user = user;
        this.board = board;
        this.comment = comment;
        this.parent = parent;
    }

    // 댓글 생성 정적 팩터리
    public static Comment createComment(User user, Board board, String comment) {
        return new Comment(user, board, comment, null);
    }

    // 대댓글 생성 정적 팩터리
    public static Comment createReply(User user, Comment parent, String comment) {
        if (parent.getParent() != null) throw new CommentException("대댓글의 대댓글은 허용하지 않음.");
        return new Comment(user, parent.getBoard(), comment, parent);
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
}
