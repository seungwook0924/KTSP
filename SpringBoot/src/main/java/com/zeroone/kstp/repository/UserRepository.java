package com.zeroone.kstp.repository;

import com.zeroone.kstp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByStudentNumber(String studentNumber);

    Optional<User> findByStudentNumber(String studentNumber);

    Optional<User> findByEMail(String email);
}
