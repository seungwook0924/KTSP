package com.seungwook.ktsp.domain.board.entity;

import com.seungwook.ktsp.domain.board.entity.enums.MainType;
import com.seungwook.ktsp.domain.board.entity.enums.SubType;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "board_type", discriminatorType = DiscriminatorType.STRING)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MainType mainType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubType subType;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int hits;
}
