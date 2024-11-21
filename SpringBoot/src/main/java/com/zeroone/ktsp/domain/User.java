package com.zeroone.ktsp.domain;

import com.zeroone.ktsp.enumeration.UserLevel;
import com.zeroone.ktsp.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role", columnDefinition = "role")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserRole role;

    @Column(name = "student_number", unique = true, length = 9)
    private String studentNumber;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(name = "name", nullable = false, length = 5)
    private String name;

    @Column(nullable = false, length = 13)
    private String tel;

    @Column(name = "email", nullable = false, unique = true)
    private String EMail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "level", columnDefinition = "level")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserLevel level;

    @Column(nullable = false, length = 20)
    private String major;

    @Column(name = "last_grades", precision = 3, scale = 2, nullable = false)
    private BigDecimal lastGrades;

    // User와 board는 1:N 관계
    // 한 명의 사용자는 여러 게시글을 작성할 수 있다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 유저 삭제시 모든 게시글 삭제
    private List<Board> boards;

    // User와 Wating은 1:N 관계
    // 한 명의 사용자는 여러 대기줄에 참여할 수 있다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 유저 삭제시 모든 대기줄 삭제
    private List<Wating> watings;

    // User와 Team은 1:N 관계
    // 한 명의 사용자는 여러 팀에 소속될 수 있다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 유저 삭제시 모든 팀 삭제
    private List<Team> teams;

    // User와 Comment는 1:N 관계
    // 한 명의 사용자는 여러 댓글을 달 수 있다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 유저 삭제시 모든 댓글 삭제
    private List<Comment> comments;

    // User와 Report 1:N 관계
    // 한 명의 사용자는 여러 리포트를 작성할 수 있다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 유저 삭제시 모든 리포트 삭제
    private List<Report> reports;


    // UserDetails 메서드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> role.name());
    }

    @Override
    public String getUsername() {
        return studentNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
