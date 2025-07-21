package com.seungwook.ktsp.domain.board.common.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentRequest {

    private final long boardId;
    private final String comment;

    public CommentRequest(long boardId, String comment) {
        this.boardId = boardId;
        this.comment = comment;
    }
}
