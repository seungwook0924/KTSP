package com.seungwook.ktsp.domain.board.function.etc.common.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public BoardResponse(String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
