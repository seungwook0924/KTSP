package com.seungwook.ktsp.domain.comment.controller;

import com.seungwook.ktsp.domain.comment.dto.request.CommentRequest;
import com.seungwook.ktsp.domain.comment.service.CommentCommandService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
