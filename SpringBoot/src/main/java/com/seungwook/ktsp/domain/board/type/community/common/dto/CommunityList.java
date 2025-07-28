package com.seungwook.ktsp.domain.board.type.community.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommunityList {

    private final long boardId;

    private final String title;

    private final int hits;

    @JsonFormat(pattern = "yy.MM.dd")
    private final LocalDateTime createdAt;
}
