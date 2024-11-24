package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.domain.Waiting;
import com.zeroone.ktsp.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingService
{
    private final WaitingRepository waitingRepository;

    public Waiting save(Board board, User user, String content)
    {
        Waiting waiting = Waiting.builder()
                .board(board)
                .user(user)
                .content(content)
                .build();

        Waiting savedWaiting = waitingRepository.save(waiting);
        log.info("대기열 저장 완료 - 게시판 Id={}, userId={}, content={}", board.getId(), user.getId(), content);

        return savedWaiting;
    }

    // 게시글과 사용자에 대해 지원 여부 확인
    public boolean existsByBoardAndUser(Board board, User user)
    {
        return waitingRepository.existsByBoardAndUser(board, user);
    }
}
