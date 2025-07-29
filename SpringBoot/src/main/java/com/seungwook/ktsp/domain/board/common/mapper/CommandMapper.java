package com.seungwook.ktsp.domain.board.common.mapper;

import com.seungwook.ktsp.domain.board.common.dto.response.CommandResponse;
import com.seungwook.ktsp.domain.board.common.entity.Board;

public class CommandMapper {

    public static CommandResponse toCommandResponse(Board board) {
        return new CommandResponse(board.getId(), board.getSubType());
    }
}
