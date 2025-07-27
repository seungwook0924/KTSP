package com.seungwook.ktsp.domain.board.common.repository.hits;

public interface HitsIncrement {

    // 게시판 조회수 증가
    void increaseHits(Long boardId);
}
