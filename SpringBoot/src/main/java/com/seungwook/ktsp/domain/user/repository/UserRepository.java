package com.seungwook.ktsp.domain.user.repository;

import com.seungwook.ktsp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByStudentNumber(String studentNumber);

    Optional<User> findByStudentNumber(String studentNumber);
}
