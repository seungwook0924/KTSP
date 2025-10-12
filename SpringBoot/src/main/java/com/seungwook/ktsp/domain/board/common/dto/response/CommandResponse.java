package com.seungwook.ktsp.domain.board.common.dto.response;

import com.seungwook.ktsp.domain.board.common.entity.enums.SubType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandResponse {
    private final long boardId;
    private final SubType subType;
}
