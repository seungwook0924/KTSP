package com.seungwook.ktsp.domain.comment.controller;

import com.seungwook.ktsp.domain.comment.dto.request.CommentReplyRequest;
import com.seungwook.ktsp.domain.comment.dto.request.CommentRequest;
import com.seungwook.ktsp.domain.comment.dto.request.CommentUpdateRequest;
import com.seungwook.ktsp.domain.comment.service.CommentCommandService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service/comment")
public class CommentCommandController {

    private final CommentCommandService commentCommandService;

    @PostMapping
    public ResponseEntity<Response<Void>> registerComment(@Valid @RequestBody CommentRequest request) {

        commentCommandService.registerComment(AuthHandler.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("댓글 등록 성공")
                .build());
    }

    @PostMapping("/reply")
    public ResponseEntity<Response<Void>> reply(@Valid @RequestBody CommentReplyRequest request) {

        commentCommandService.registerReply(AuthHandler.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("대댓글 등록 성공")
                .build());
    }

    @PatchMapping
    public ResponseEntity<Response<Void>> updateComment(@Valid @RequestBody CommentUpdateRequest request) {

        commentCommandService.updateComment(request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("댓글 수정 성공")
                .build());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response<Void>> deleteComment(@PathVariable long commentId) {

        commentCommandService.deleteComment(commentId);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("댓글 삭제 성공")
                .build());
    }
}
