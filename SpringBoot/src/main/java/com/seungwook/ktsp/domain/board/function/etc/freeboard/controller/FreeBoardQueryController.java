package com.seungwook.ktsp.domain.board.function.etc.freeboard.controller;

import com.seungwook.ktsp.domain.board.function.etc.common.dto.response.BoardResponse;
import com.seungwook.ktsp.domain.board.function.etc.freeboard.entity.FreeBoard;
import com.seungwook.ktsp.domain.board.function.etc.freeboard.service.FreeBoardQueryService;
import com.seungwook.ktsp.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service/board/free-board")
public class FreeBoardQueryController {

    private final FreeBoardQueryService freeBoardQueryService;

    @GetMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> getFreeBoard(@PathVariable long boardId) {
        FreeBoard freeBoard = freeBoardQueryService.getFreeBoard(boardId);

        BoardResponse response = new BoardResponse(freeBoard.getTitle(), freeBoard.getContent(), freeBoard.getCreatedAt(), freeBoard.getModifiedAt());

        return ResponseEntity.ok(Response.<BoardResponse>builder()
                .message("자유게시판 게시글 등록 성공")
                .data(response)
                .build());
    }
}
