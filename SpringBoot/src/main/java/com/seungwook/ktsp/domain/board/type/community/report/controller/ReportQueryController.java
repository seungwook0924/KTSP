package com.seungwook.ktsp.domain.board.type.community.report.controller;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.board.type.community.common.dto.response.CommunityListResponse;
import com.seungwook.ktsp.domain.board.type.community.common.dto.response.CommunityResponse;
import com.seungwook.ktsp.domain.board.type.community.common.mapper.CommunityMapper;
import com.seungwook.ktsp.domain.board.type.community.report.entity.Report;
import com.seungwook.ktsp.domain.board.type.community.report.service.ReportQueryService;
import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.comment.service.CommentQueryService;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.service.FileService;
import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.domain.user.service.UserQueryService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "리포트", description = "리포트 관련 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/service/board/report")
public class ReportQueryController {

    private final FileService fileService;
    private final ReportQueryService reportQueryService;
    private final UserQueryService userQueryService;
    private final CommentQueryService commentQueryService;

    @Operation(summary = "모든 리포트 조회", description = "일반 유저라면 본인이 작성한 것만, 관리자라면 모든 리포트 조회, 페이징 크기 = 15")
    @GetMapping
    public ResponseEntity<Response<Page<CommunityListResponse>>> viewReportList(@RequestParam(defaultValue = "0") int page) {

        Page<CommunityListResponse> response;

        // 관리자라면 모든 리포트, 일반 유저라면 본인이 작성한 것만 리턴
        if (AuthHandler.getUserRole() == UserRole.ADMIN)
            response = CommunityMapper.toReportList(reportQueryService.getAllReports(page));
        else
            response = CommunityMapper.toReportList(reportQueryService.getUserReports(AuthHandler.getUserId(), page));

        return ResponseEntity.ok(Response.<Page<CommunityListResponse>>builder()
                .message("모든 리포트 조회 성공")
                .data(response)
                .build());
    }

    @Operation(summary = "리포트 상세 조회", description = "특정 리포트 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<Response<CommunityResponse>> viewReport(@PathVariable long boardId) {

        // 리포트
        Report report = reportQueryService.getReport(boardId);

        // 작성자
        Writer writer = userQueryService.getWriter(report.getUser().getId());

        // 첨부파일
        List<AttachedFileInfo> attachedFileInfos = fileService.getAttachedFileDownloadPath(boardId);

        // 댓글
        List<CommentResponse> comments = commentQueryService.getBoardComments(report);

        // 수정 및 삭제 권한 소유 여부
        boolean manageable = AuthHandler.hasManagePermission(writer.getUserId());

        // 응답 본문(작성자 + 리포트 + 첨부파일)
        CommunityResponse response = CommunityMapper.toReportResponse(writer, report, attachedFileInfos, manageable, comments);

        return ResponseEntity.ok(Response.<CommunityResponse>builder()
                .message("리포트 조회 성공")
                .data(response)
                .build());
    }
}
