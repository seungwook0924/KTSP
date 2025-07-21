package com.seungwook.ktsp.domain.board.function.etc.freeboard.controller;

import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.RegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.freeboard.service.FreeBoardCommandService;
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
@RequestMapping("/service/board/free-board")
public class FreeboardCommandController {

    private final FreeBoardCommandService freeBoardCommandService;

    @PostMapping
    public ResponseEntity<Response<Void>> saveFreeBoard(@Valid @RequestBody RegisterRequest request) {

        freeBoardCommandService.freeBoardRegister(AuthHandler.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("자유게시판 게시글 등록 성공")
                .build());
    }
}
