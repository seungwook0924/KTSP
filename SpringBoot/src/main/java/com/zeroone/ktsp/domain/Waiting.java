package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "waiting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Wating과 Board는 1:1 관계
    // 하나의 승인 대기 줄은 하나의 게시글을 가진다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //1:1 매핑, 지연로딩, 게시글 삭제시 대기줄 삭제
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // Wating과 User는 N:1 관계
    // 여러 대기줄에 하나의 유저가 참여할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT") // TEXT 타입으로 생성되도록 명시
    private String content;
}
