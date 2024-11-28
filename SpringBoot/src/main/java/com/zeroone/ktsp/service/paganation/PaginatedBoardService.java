package com.zeroone.ktsp.service.paganation;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.enumeration.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaginatedBoardService
{
    Page<Board> findBoardsByTypeWithPaging(BoardType boardType, Pageable pageable); // 페이지네이션 메서드
}