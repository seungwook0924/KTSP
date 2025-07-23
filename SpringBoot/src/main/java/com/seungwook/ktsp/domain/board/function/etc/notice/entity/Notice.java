package com.seungwook.ktsp.domain.board.function.etc.notice.entity;

import com.seungwook.ktsp.domain.board.common.board.entity.Board;
import com.seungwook.ktsp.domain.board.common.board.entity.enums.MainType;
import com.seungwook.ktsp.domain.board.common.board.entity.enums.SubType;
import com.seungwook.ktsp.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("NOTICE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Board {

    // 생성자
    private Notice(User user, MainType mainType, SubType subType, String title, String content) {
        super(user, mainType, subType, title, content);
    }

    // 정적 팩터리
    public static Notice createNotice(User user, String title, String content) {
        return new Notice(user, MainType.ETC, SubType.NOTICE, title, content);
    }

    // 게시글 수정
    public void updateNotice(String title, String content) {
        updateTitleAndContent(title, content);
    }

    // 조회수 증가
    public void increaseHit() {
        increaseHits();
    }
}
