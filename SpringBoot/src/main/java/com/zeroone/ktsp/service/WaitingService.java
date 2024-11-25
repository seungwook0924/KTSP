package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.domain.Waiting;
import com.zeroone.ktsp.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingService
{
    private final WaitingRepository waitingRepository;

    public void save(Board board, User user, String content)
    {
        Waiting waiting = Waiting.builder()
                .board(board)
                .user(user)
                .content(content)
                .isValid(true)
                .build();

        waitingRepository.save(waiting);
    }

    public void rejectWaiting(Waiting waiting)
    {
        Waiting updateWaiting = waiting.toBuilder().isValid(false).build();
        waitingRepository.save(updateWaiting);
    }

    // 게시글과 사용자에 대해 지원 여부 확인
    public boolean existsByBoardAndUser(Board board, User user)
    {
        return waitingRepository.existsByBoardAndUser(board, user);
    }

    // Board를 바탕으로 지원자를 가져오는 메서드
    public Optional<Waiting> findByBoard(Board board)
    {
        return waitingRepository.findByBoard(board);
    }

    // Board를 기반으로 모든 Waiting 객체를 가져오는 메서드
    public List<Waiting> findAllByBoard(Board board)
    {
        return waitingRepository.findAllByBoard(board);
    }

    public Optional<Waiting> findByID(long id)
    {
        return waitingRepository.findById(id);
    }

    public void delete(Waiting waiting)
    {
        waitingRepository.delete(waiting);
    }
}
