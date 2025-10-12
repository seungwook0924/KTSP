package com.seungwook.ktsp.domain.board.type.community.report.controller;

import com.seungwook.ktsp.domain.board.common.dto.response.CommandResponse;
import com.seungwook.ktsp.domain.board.common.mapper.CommandMapper;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityRegisterRequest;
import com.seungwook.ktsp.domain.board.type.community.common.dto.request.CommunityUpdateRequest;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.board.type.community.report.service.ReportCommandService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리포트", description = "리포트 관련 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/service/board/report")
public class ReportCommandController {

    private final ReportCommandService reportCommandService;

    @Operation(summary = "리포트 등록")
    @PostMapping
    public ResponseEntity<Response<CommandResponse>> registerReport(@Valid @RequestBody CommunityRegisterRequest request) {

        Report report = reportCommandService.registerReport(AuthHandler.getUserId(), request);

        CommandResponse response = CommandMapper.toCommandResponse(report);

        return ResponseEntity.ok(Response.<CommandResponse>builder()
                .message("리포트 등록 성공")
                .data(response)
                .build());
    }

    @Operation(summary = "리포트 수정")
    @PatchMapping("/{boardId}")
    public ResponseEntity<Response<CommandResponse>> updateReport(@PathVariable long boardId, @Valid @RequestBody CommunityUpdateRequest request) {

        Report report = reportCommandService.updateReport(boardId, request);

        CommandResponse response = CommandMapper.toCommandResponse(report);

        return ResponseEntity.ok(Response.<CommandResponse>builder()
                .message("리포트 수정 성공")
                .data(response)
                .build());
    }

    @Operation(summary = "리포트 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response<Void>> deleteReport(@PathVariable long boardId) {

        reportCommandService.deleteReport(boardId);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("리포트 삭제 성공")
                .build());
    }
}
