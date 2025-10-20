package com.seungwook.ktsp.domain.board.type.learning_core.service;

import com.seungwook.ktsp.domain.board.common.service.BoardFileBindingService;
import com.seungwook.ktsp.domain.board.type.learning_core.dto.request.RegisterRequest;
import com.seungwook.ktsp.domain.board.type.learning_core.entity.LearningCore;
import com.seungwook.ktsp.domain.board.type.learning_core.repository.LearningCoreRepository;
import com.seungwook.ktsp.domain.comment.service.CommentCommandService;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningCoreCommandService {

    private final LearningCoreRepository learningCoreRepository;
    private final UserDomainService userDomainService;
    private final BoardFileBindingService boardFileBindingService;
    private final CommentCommandService commentCommandService;

    public LearningCore registerMentor(User user, RegisterRequest request) {
        LearningCore learningCore = LearningCore.registerMentor(
                user,
                request.getTitle(),
                request.getContent(),
                request.getCampus(),
                request.getTeamSize()
        );

        learningCoreRepository.save(learningCore);

        boardFileBindingService.bindFilesToBoard(learningCore, request.getContent(), request.getAttachedFiles());

        return learningCore;
    }
}
