package com.seungwook.ktsp.domain.board.common.repository.hits;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

class HitsIncrementImpl implements HitsIncrement {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void increaseHits(Long boardId) {
        em.createQuery("update Board b set b.hits = b.hits + 1 where b.id = :id")
                .setParameter("id", boardId)
                .executeUpdate();
        em.clear(); // clearAutomatically와 동일 효과
    }
}