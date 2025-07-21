package com.seungwook.ktsp.domain.board.function.etc.notice.controller;

import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardRegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardUpdateRequest;
import com.seungwook.ktsp.domain.board.function.etc.notice.service.NoticeCommandService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeCommandController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping
    public ResponseEntity<Response<Void>> saveNotice(@Valid @RequestBody BoardRegisterRequest request) {

        noticeCommandService.noticeRegister(AuthHandler.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("공지사항 등록 성공")
                .build());
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Response<Void>> updateFreeBoard(@PathVariable long boardId, @Valid @RequestBody BoardUpdateRequest request) {

        noticeCommandService.noticeUpdate(boardId, request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("공지사항 수정 성공")
                .build());
    }
}
