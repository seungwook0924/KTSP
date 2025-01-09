package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Team과 Board는 1:1 관계
    // 하나의 팀은 하나의 게시글에서 만들어진다.
    @OneToOne(fetch = FetchType.LAZY) //1:1 매핑, 지연로딩
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // Team과 User는 N:1 관계
    // 여러 팀에 하나의 유저가 소속될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid; //추방 당했는지 여부
}
