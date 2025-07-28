package com.seungwook.ktsp.domain.board.type.community.notice.controller;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.board.type.community.common.dto.CommunityList;
import com.seungwook.ktsp.domain.board.type.community.common.dto.response.CommunityResponse;
import com.seungwook.ktsp.domain.board.type.community.common.mapper.CommunityMapper;
import com.seungwook.ktsp.domain.board.type.community.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.type.community.notice.service.NoticeService;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.service.FileService;
import com.seungwook.ktsp.domain.user.mapper.UserMapper;
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

@Tag(name = "공지사항")
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/board/notice")
public class NoticeQueryController {

    private final UserQueryService userQueryService;
    private final NoticeService noticeService;
    private final FileService fileService;

    @Operation(summary = "모든 공지사항 조회", description = "모든 공지사항 조회, 페이징 크기 = 15")
    @GetMapping
    public ResponseEntity<Response<Page<CommunityList>>> viewNoticeList(@RequestParam(defaultValue = "0") int page) {

        // 모든 공지사항 조회
        Page<CommunityList> allNotice = noticeService.getAllNotice(page);

        return ResponseEntity.ok(Response.<Page<CommunityList>>builder()
                .message("공지사항 조회 성공")
                .data(allNotice)
                .build());
    }

    @Operation(summary = "공지사항 상세 조회", description = "특정 공지사항 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<Response<CommunityResponse>> viewNotice(@PathVariable long boardId) {

        // 공지사항
        Notice notice = noticeService.getNotice(boardId);

        // 작성자
        Writer writer = UserMapper.toWriter(userQueryService.getWriterInfo(notice.getUser().getId()));

        // 첨부파일
        List<AttachedFileInfo> attachedFileInfos = fileService.getAttachedFileDownloadPath(boardId);

        // 수정 및 삭제 권한 소유 여부
        boolean manageable = AuthHandler.hasManagePermission(writer.getUserId());

        // 응답 본문(작성자 + 공지사항 + 첨부파일)
        CommunityResponse response = CommunityMapper.toNoticeResponse(writer, notice, attachedFileInfos, manageable);

        return ResponseEntity.ok(Response.<CommunityResponse>builder()
                .message("공지사항 조회 성공")
                .data(response)
                .build());
    }
}
