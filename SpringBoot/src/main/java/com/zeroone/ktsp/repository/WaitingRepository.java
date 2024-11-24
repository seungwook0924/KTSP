package com.zeroone.ktsp.repository;

import com.zeroone.ktsp.domain.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
