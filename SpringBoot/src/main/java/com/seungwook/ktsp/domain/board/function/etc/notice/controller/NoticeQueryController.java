package com.seungwook.ktsp.domain.board.function.etc.notice.controller;

import com.seungwook.ktsp.domain.board.function.etc.common.dto.response.BoardResponse;
import com.seungwook.ktsp.domain.board.function.etc.common.mapper.EtcResponseMapper;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.function.etc.notice.service.NoticeQueryService;
import com.seungwook.ktsp.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/board/notice")
public class NoticeQueryController {

    private final NoticeQueryService noticeQueryService;

    @GetMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> viewNotice(@PathVariable long boardId) {

        Notice notice = noticeQueryService.getNotice(boardId);

        BoardResponse response = EtcResponseMapper.toNoticeResponse(notice);

        return ResponseEntity.ok(Response.<BoardResponse>builder()
                .message("공지사항 조회 성공")
                .data(response)
                .build());
    }
}
