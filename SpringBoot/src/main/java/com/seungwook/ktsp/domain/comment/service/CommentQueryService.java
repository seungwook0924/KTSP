package com.seungwook.ktsp.domain.comment.service;

import com.seungwook.ktsp.domain.comment.service.domain.CommentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentDomainService commentDomainService;

}
