package com.seungwook.ktsp.domain.comment.mapper;

import com.seungwook.ktsp.domain.comment.dto.CommentInfo;
import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.global.auth.support.AuthHandler;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static List<CommentResponse> toCommentResponse(List<CommentInfo> comments) {
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getCommentId(),
                        comment.getParentCommentId(),
                        comment.getUserId(),
                        comment.getWriter(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        isEdited(comment),
                        AuthHandler.hasManagePermission(comment.getUserId())
                )
                )
                .collect(Collectors.toList());
    }

    // 작성 시각과 수정 시각이 다르면 편집됨
    private static boolean isEdited(CommentInfo comment) {
        return !comment.getCreatedAt().isEqual(comment.getModifiedAt());
    }
}
