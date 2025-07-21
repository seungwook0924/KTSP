package com.seungwook.ktsp.domain.board.common.comment.controller;

import com.seungwook.ktsp.domain.board.common.comment.dto.request.CommentRequest;
import com.seungwook.ktsp.domain.board.common.comment.service.CommentCommandService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
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
    public ResponseEntity<Response<Void>> registerComment(@RequestBody CommentRequest request) {
        commentCommandService.registerComment(AuthHandler.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("자유게시판 댓글 등록 성공")
                .build());
    }
}
