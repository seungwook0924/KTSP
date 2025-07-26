package com.seungwook.ktsp.domain.board.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Writer {
    private final String name;
    private final String major;
    private final String studentNumber;
}
