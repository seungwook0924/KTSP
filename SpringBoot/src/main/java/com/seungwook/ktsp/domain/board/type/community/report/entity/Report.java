package com.seungwook.ktsp.domain.board.type.community.report.entity;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.board.common.entity.enums.MainType;
import com.seungwook.ktsp.domain.board.common.entity.enums.SubType;
import com.seungwook.ktsp.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("REPORT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends Board {

    // 생성자
    private Report(User user, MainType mainType, SubType subType, String title, String content) {
        super(user, mainType, subType, title, content);
    }

    // 정적 팩터리
    public static Report createReport(User user, String title, String content) {
        return new Report(user, MainType.COMMUNITY, SubType.REPORT, title, content);
    }

    // 게시글 수정
    public void updateReport(String title, String content) {
        updateTitleAndContent(title, content);
    }
}
