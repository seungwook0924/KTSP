package com.seungwook.ktsp.domain.board.freeboard.entity;

import com.seungwook.ktsp.domain.board.entity.Board;
import com.seungwook.ktsp.domain.board.entity.enums.MainType;
import com.seungwook.ktsp.domain.board.entity.enums.SubType;
import com.seungwook.ktsp.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("FREE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FreeBoard extends Board {

    // 생성자
    private FreeBoard(User user, MainType mainType, SubType subType, String title, String content) {
        super(user, mainType, subType, title, content);
    }

    // 정적 팩터리
    public static FreeBoard createFreeBoard(User user, String title, String content) {
        return new FreeBoard(user, MainType.ETC, SubType.FREE, title, content);
    }

    // 게시글 수정
    public void updateFreeBoard(String title, String content) {
        updateTitleAndContent(title, content);
    }

    // 조회수 증가
    public void increaseHit() {
        increaseHits();
    }
}
