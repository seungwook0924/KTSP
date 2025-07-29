package com.seungwook.ktsp.domain.board.type.community.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommunityResponse {

    private final Writer writer;

    private final long boardId;

    private final int hits;

    private final String title;

    private final String content;

    private final boolean manageable;

    @JsonFormat(pattern = "yy.MM.dd-HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yy.MM.dd-HH:mm:ss")
    private final LocalDateTime modifiedAt;

    private final List<CommentResponse> comments;

    private final List<AttachedFileInfo> attachedFiles;
}