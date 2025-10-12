package com.seungwook.ktsp.domain.board.type.community.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommunityList {
    private final long boardId;
    private final String title;
    private final int hits;
    private final String writer;
    private final LocalDateTime createdAt;
}
