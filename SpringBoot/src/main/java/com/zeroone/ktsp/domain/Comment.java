package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Comment와 User는 N:1 관계
    // 여러 댓글은 하나의 유저에 의해 작성될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Comment와 Board는 N:1 관계
    // 여러개의 댓글은 하나의 게시글에 달릴 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //1:1 매핑, 지연로딩
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    // 자식 댓글
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    // 댓글 내용
    @Column(nullable = false, length = 100)
    private String content;

    // 댓글 생성 시간
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 댓글 깊이
    @Column(nullable = false)
    private int depth;

    // 생성자 빌더에 깊이 설정
    @Builder
    public Comment(User user, Board board, Comment parentComment, String content, LocalDateTime createdAt) {
        this.user = user;
        this.board = board;
        this.parentComment = parentComment;
        this.content = content;
        this.createdAt = createdAt;

        // 부모 댓글이 있으면 깊이를 부모 깊이 + 1로 설정, 없으면 0
        this.depth = (parentComment != null) ? parentComment.getDepth() + 1 : 0;

        // 깊이가 2를 초과하면 예외를 발생시킴
        if (this.depth > 2) {
            throw new IllegalArgumentException("댓글 최대 깊이 초과");
        }
    }
}
