package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 특정 BoardType의 게시글 목록 반환
    public List<Board> findBoardsByType(BoardType type)
    {
        return boardRepository.findByType(type);
    }

    public Board save(Board newBoard)
    {
        return boardRepository.save(newBoard);
    }

    public Optional<Board> findById(long id)
    {
        return boardRepository.findById(id);
    }
}
