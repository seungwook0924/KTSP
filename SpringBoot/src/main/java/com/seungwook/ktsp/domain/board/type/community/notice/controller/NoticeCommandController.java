package com.seungwook.ktsp.domain.board.type.community.notice.controller;

import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityRegisterRequest;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityUpdateRequest;
import com.seungwook.ktsp.domain.board.type.community.notice.service.NoticeCommandService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeCommandController {

    private final NoticeCommandService noticeCommandService;

    @Operation(summary = "공지사항 등록", description = "공지사항은 Admin만 등록 가능")
    @PostMapping
    public ResponseEntity<Response<Void>> registerNotice(@Valid @RequestBody CommunityRegisterRequest request) {

        noticeCommandService.registerNotice(AuthHandler.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("공지사항 등록 성공")
                .build());
    }

    @Operation(summary = "공지사항 수정")
    @PatchMapping("/{boardId}")
    public ResponseEntity<Response<Void>> updateNotice(@PathVariable long boardId, @Valid @RequestBody CommunityUpdateRequest request) {

        noticeCommandService.updateNotice(boardId, request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("공지사항 수정 성공")
                .build());
    }

    @Operation(summary = "공지사항 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response<Void>> deleteNotice(@PathVariable long boardId) {

        noticeCommandService.deleteNotice(boardId);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("공지사항 삭제 성공")
                .build());
    }
}
