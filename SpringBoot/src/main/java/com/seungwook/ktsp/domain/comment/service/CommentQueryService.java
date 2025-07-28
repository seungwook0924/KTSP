package com.seungwook.ktsp.domain.comment.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.comment.dto.CommentInfo;
import com.seungwook.ktsp.domain.comment.dto.response.CommentResponse;
import com.seungwook.ktsp.domain.comment.mapper.CommentMapper;
import com.seungwook.ktsp.domain.comment.service.domain.CommentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentDomainService commentDomainService;

    public List<CommentResponse> getBoardComments(Board board) {
        List<CommentInfo> comments = commentDomainService.getComments(board.getId());

        return CommentMapper.toCommentResponse(comments);
    }
}
