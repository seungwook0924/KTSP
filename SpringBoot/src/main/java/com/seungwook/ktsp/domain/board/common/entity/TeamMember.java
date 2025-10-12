package com.seungwook.ktsp.domain.board.common.entity;

import com.seungwook.ktsp.domain.board.common.entity.enums.TeamJoinStatus;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    User는 여러 Board에 TeamMember가 될 수 있지만,
    같은 Board에 중복으로 TeamMember가 될 수 없다.
*/
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_member", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "board_id"}))
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TeamMember와 Board는 N:1 관계
    // 여러 TeamMember는 하나의 Board에 속한다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // TeamMember와 User는 N:1 관계
    // 하나의 User는 여러 TeamMember가 될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TeamJoinStatus status;

    @Column(name = "introduction", nullable = true, length = 255)
    private String introduction;

    // 생성자
    private TeamMember(Board board, User user, TeamJoinStatus status, String introduction) {
        this.board = board;
        this.user = user;
        this.status = status;
        this.introduction = introduction;
    }

    // 최초 Board 생성자는 TeamMember 등록
    public static TeamMember BoardConstructorRegister(Board board, User user) {
        return new TeamMember(board, user, TeamJoinStatus.APPROVED, null);
    }

    // 팀원 추가(최초 수락 대기중)
    public static TeamMember addTeamMember(Board board, User user, String introduction) {
        return new TeamMember(board, user, TeamJoinStatus.PENDING, introduction);
    }

    // 팀원 상태를 소속됨으로 변경
    public void approvedTeamMember() {
        this.status = TeamJoinStatus.APPROVED;
    }

    // 팀원 상태를 거절됨으로 변경
    public void rejectedTeamMember() {
        this.status = TeamJoinStatus.REJECTED;
    }

    // 팀원 상태를 추방됨으로 변경
    public void expelledTeamMember() {
        this.status = TeamJoinStatus.EXPELLED;
    }

    // 소개글 수정
    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
