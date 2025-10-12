package com.seungwook.ktsp.domain.comment.entity;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.comment.exception.CommentException;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Comment와 User는 N:1 관계
    // 여러 댓글은 하나의 유저에 의해 작성될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Comment와 Board는 N:1 관계
    // 여러개의 댓글은 하나의 게시글에 달릴 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // null이면 일반 댓글, not null이면 대댓글

    // 댓글 내용
    @Column(nullable = false, length = 255)
    private String comment;

    // 양방향 매핑(부모댓글 삭제시 자식댓글도 자동 삭제)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> replies = new ArrayList<>();

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
        Comment reply = new Comment(user, parent.getBoard(), comment, parent);
        parent.replies.add(reply);
        return reply;
    }

    // 댓글 수정
    public void updateComment(String comment) {
        this.comment = comment;
    }
}
