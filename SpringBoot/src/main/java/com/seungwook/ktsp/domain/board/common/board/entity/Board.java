package com.seungwook.ktsp.domain.board.common.board.entity;

import com.seungwook.ktsp.domain.board.common.board.entity.enums.MainType;
import com.seungwook.ktsp.domain.board.common.board.entity.enums.SubType;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "board_type", discriminatorType = DiscriminatorType.STRING)
@SQLDelete(sql = "UPDATE board SET deleted = true WHERE id = ?") // 삭제시 delete=true로 변경(soft delete)
public abstract class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Board와 User는 N:1 관계
    // 여러개의 Board는 하나의 User에 의해 작성될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MainType mainType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubType subType;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int hits;

    @Column(nullable = false)
    private boolean deleted = false;

    // 생성자
    protected Board(User user, MainType mainType, SubType subType, String title, String content) {
        this.user = user;
        this.mainType = mainType;
        this.subType = subType;
        this.title = title;
        this.content = content;
        this.hits = 0;
        this.deleted = false;
    }

    // title, content 수정
    protected void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수 증가
    protected void increaseHits() {
        this.hits++;
    }
}
