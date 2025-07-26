package com.seungwook.ktsp.domain.board.function.etc.notice.controller;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.response.BoardResponse;
import com.seungwook.ktsp.domain.board.function.etc.common.mapper.EtcResponseMapper;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.function.etc.notice.service.NoticeService;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.service.FileService;
import com.seungwook.ktsp.domain.user.mapper.UserMapper;
import com.seungwook.ktsp.domain.user.service.UserQueryService;
import com.seungwook.ktsp.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/board/notice")
public class NoticeQueryController {

    private final UserQueryService userQueryService;
    private final NoticeService noticeService;
    private final FileService fileService;

    @GetMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> viewNotice(@PathVariable long boardId) {

        Notice notice = noticeService.getNotice(boardId);

        Writer writer = UserMapper.toWriter(userQueryService.getWriterInfo(notice.getUser().getId()));

        List<AttachedFileInfo> attachedFileInfos = fileService.getAttachedFileDownloadPath(boardId);

        BoardResponse response = EtcResponseMapper.toNoticeResponse(writer, notice, attachedFileInfos);

        return ResponseEntity.ok(Response.<BoardResponse>builder()
                .message("공지사항 조회 성공")
                .data(response)
                .build());
    }
}
