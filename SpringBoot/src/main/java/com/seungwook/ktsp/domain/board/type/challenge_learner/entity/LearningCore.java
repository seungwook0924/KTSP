package com.seungwook.ktsp.domain.board.type.challenge_learner.entity;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.board.common.entity.enums.MainType;
import com.seungwook.ktsp.domain.board.common.entity.enums.Semester;
import com.seungwook.ktsp.domain.board.common.entity.enums.SubType;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.entity.enums.Campus;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("CHALLENGE_LEARNER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningCore extends Board {

    @Column(name = "campus", nullable = false)
    private Campus campus;

    @Column(name = "team_size", nullable = false, columnDefinition = "TINYINT")
    private int teamSize;

    @Column(name = "is_closed", nullable = false)
    private boolean isClosed;

    @Column(name = "semester", nullable = false)
    private Semester semester;

    @Builder(access = AccessLevel.PRIVATE)
    private LearningCore(User user, MainType mainType, SubType subType, String title, String content, Campus campus, int teamSize, boolean isClosed) {
        super(user, mainType, subType, title, content);
        this.campus = campus;
        this.teamSize = teamSize;
        this.isClosed = isClosed;
        this.semester = Semester.resolveByDate();
    }

    // 가르치미 등록
    public LearningCore registerMentor(User user, String title, String content, Campus campus, int teamSize) {
        return LearningCore.builder()
                .user(user)
                .mainType(MainType.LEARNING_CORE)
                .subType(SubType.MENTOR)
                .title(title)
                .content(content)
                .campus(campus)
                .teamSize(teamSize)
                .isClosed(false)
                .build();
    }

    // 배우미 등록
    public LearningCore registerMentee(User user, String title, String content, Campus campus, int teamSize) {
        return LearningCore.builder()
                .user(user)
                .mainType(MainType.LEARNING_CORE)
                .subType(SubType.MENTEE)
                .title(title)
                .content(content)
                .campus(campus)
                .teamSize(teamSize)
                .isClosed(false)
                .build();
    }

    // 게시글 수정
    public void update(String title, String content, Campus campus, int teamSize) {
        updateTitleAndContent(title, content);
        this.campus = campus;
        this.teamSize = teamSize;
    }

    // 지원 마감
    public void close() {
        this.isClosed = true;
    }
}
