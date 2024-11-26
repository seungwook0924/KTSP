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
public class BoardService
{
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

    // 게시글 삭제
    public void deleteById(long id)
    {
        if (boardRepository.existsById(id)) boardRepository.deleteById(id); // 삭제 전에 존재 여부 확인
        else throw new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다. ID: " + id);
    }
}
