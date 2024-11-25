package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Team;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void save(Board board, User user)
    {
        Team team = Team.builder()
                .board(board)
                .user(user)
                .isValid(true)
                .build();
        teamRepository.save(team);
    }

    public void expulsionTeam(Team team)
    {
        Team updateTeam = team.toBuilder().isValid(false).build();
        teamRepository.save(updateTeam);
    }

    // 특정 board에 매핑된 모든 Team 객체를 반환하는 메서드
    public List<Team> findTeamsByBoard(Board board)
    {
        return teamRepository.findAllByBoardId(board.getId());
    }

    public Optional<Team> findById(long id)
    {
        return teamRepository.findById(id);
    }
}
