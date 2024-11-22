package com.zeroone.ktsp.domain;

import com.zeroone.ktsp.enumeration.BoardType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Board와 User는 N:1 관계
    // 여러 게시글은 한명의 사용자에 의해 작성될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type", columnDefinition = "type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private BoardType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @Column(nullable = false)
    private long hits;

    @Column(name = "team_size", nullable = false)
    private Byte teamSize;

    // Board와 File은 1:N 관계
    // 하나의 게시글은 여러 파일을 가질 수 있다.
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 게시글 삭제시 모든 파일 삭제
    private List<FileMapping> fileMappings;

    // Board와 Comment는 1:N 관계
    // 하나의 게시글은 여러 댓글을 가질 수 있다.
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 게시글 삭제시 모든 댓글 삭제
    private List<Comment> comments;

    //조회수 증가 메서드
    public void incrementHits()
    {
        this.hits++;
    }
}
