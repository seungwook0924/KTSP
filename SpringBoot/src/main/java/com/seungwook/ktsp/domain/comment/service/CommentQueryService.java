package com.seungwook.ktsp.domain.comment.service;

import com.seungwook.ktsp.domain.board.common.service.BoardDomainService;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentDomainService commentDomainService;
    private final BoardDomainService boardDomainService;
    private final UserDomainService userDomainService;

}
