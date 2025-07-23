package com.seungwook.ktsp.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentDomainService commentDomainService;

}
