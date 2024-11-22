package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Team;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService
{
    private final TeamRepository teamRepository;

    // 특정 board_id에 매핑된 user 수 반환
    public Byte getUserCountByBoardId(long boardId)
    {
        return teamRepository.countUsersByBoardId(boardId);
    }

    public void save(User user, Board board)
    {
        Team team = Team.builder()
                .board(board)
                .user(user)
                .build();
        teamRepository.save(team);
    }
}
