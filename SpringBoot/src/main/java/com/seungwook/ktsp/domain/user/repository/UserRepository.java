package com.seungwook.ktsp.domain.user.repository;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.querydsl.UserQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository {

    boolean existsByEmail(String email);

    boolean existsByStudentNumber(String studentNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    // studentNumber를 바탕으로 탈퇴하지 않은 User 리턴
    @Query("SELECT u FROM User u WHERE u.studentNumber = :studentNumber AND u.status <> 'WITHDRAWN'")
    Optional<User> findByStudentNumberExceptWithdrawn(String studentNumber);

    // email를 바탕으로 탈퇴하지 않은 user 리턴
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status <> 'WITHDRAWN'")
    Optional<User> findByEmailExceptWithdrawn(String email);

    // userId를 바탕으로 Active인 User 조회
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.status = 'ACTIVE'")
    Optional<User> findActiveUserById(Long userId);

    // userId를 바탕으로 탈퇴하지 않은 User 리턴
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.status <> 'WITHDRAWN'")
    Optional<User> findByIdExceptWithdrawn(Long userId);
}
