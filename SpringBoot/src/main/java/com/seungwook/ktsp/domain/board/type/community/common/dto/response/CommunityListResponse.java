package com.seungwook.ktsp.domain.board.type.community.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommunityListResponse {

    private final int number;

    private final long boardId;

    private final String title;

    private final int hits;

    private final String writer;

    @JsonFormat(pattern = "yy.MM.dd")
    private final LocalDateTime createdAt;
}
