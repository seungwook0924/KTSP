package com.seungwook.ktsp.domain.board.function.etc.common.dto.response;

import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponse {

    private final long boardId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<CommentResponse> comments;
    private final List<AttachedFileInfo> attachedFiles;

    public BoardResponse(long boardId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<CommentResponse> comments, List<AttachedFileInfo> attachedFiles) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.comments = comments;
        this.attachedFiles = attachedFiles;
    }
}