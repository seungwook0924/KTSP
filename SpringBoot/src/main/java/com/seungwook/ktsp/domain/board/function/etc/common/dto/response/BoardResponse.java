package com.seungwook.ktsp.domain.board.function.etc.common.dto.response;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BoardResponse {

    private final Writer writer;
    private final long boardId;
    private final int hits;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<CommentResponse> comments;
    private final List<AttachedFileInfo> attachedFiles;
}