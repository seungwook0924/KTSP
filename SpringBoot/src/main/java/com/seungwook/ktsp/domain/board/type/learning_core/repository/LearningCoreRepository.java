package com.seungwook.ktsp.domain.board.type.learning_core.repository;

import com.seungwook.ktsp.domain.board.common.repository.hits.HitsIncrement;
import com.seungwook.ktsp.domain.board.type.learning_core.entity.LearningCore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningCoreRepository extends JpaRepository<LearningCore, Long>, HitsIncrement {
}
