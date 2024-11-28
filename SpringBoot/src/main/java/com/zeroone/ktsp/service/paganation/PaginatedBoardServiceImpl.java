package com.zeroone.ktsp.service.paganation;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaginatedBoardServiceImpl implements PaginatedBoardService
{
    private final BoardRepository boardRepository;

    @Autowired
    public PaginatedBoardServiceImpl(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    @Override
    public Page<Board> findBoardsByTypeWithPaging(BoardType boardType, Pageable pageable)
    {
        return boardRepository.findByType(boardType, pageable);
    }
}