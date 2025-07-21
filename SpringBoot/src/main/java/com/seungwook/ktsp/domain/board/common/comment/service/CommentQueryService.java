package com.seungwook.ktsp.domain.board.common.comment.service;

import com.seungwook.ktsp.domain.board.common.board.service.BoardDomainService;
import com.seungwook.ktsp.domain.board.common.comment.entity.Comment;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentDomainService commentDomainService;
    private final BoardDomainService boardDomainService;
    private final UserDomainService userDomainService;

    public List<Comment> getFreeBoardComment(Notice notice) {
        return commentDomainService.findByFreeBoardAndParentIsNull(notice);
    }

}
