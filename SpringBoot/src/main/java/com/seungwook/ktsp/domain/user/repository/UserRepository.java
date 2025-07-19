package com.seungwook.ktsp.domain.user.repository;

import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByStudentNumber(String studentNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    // studentNumber를 밭탕으로 탈퇴하지 않은 User 리턴
    @Query("SELECT u FROM User u WHERE u.studentNumber = :studentNumber AND u.status <> 'WITHDRAWN'")
    Optional<User> findByStudentNumberExceptWithdrawn(@Param("studentNumber") String studentNumber);

    // email를 바탕으로 탈퇴하지 않은 uer 리턴
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status <> 'WITHDRAWN'")
    Optional<User> findByEmailExceptWithdrawn(@Param("email") String email);

    // userId를 바탕으로 탈퇴하지 않은 User 리턴
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status <> 'WITHDRAWN'")
    Optional<User> findByIdExceptWithdrawn(@Param("id") Long userId);

    // userId를 바탕으로 User 프로필 조회
    @Query("""
    SELECT new com.seungwook.ktsp.domain.user.dto.UserProfile(
        u.name,
        u.major,
        u.studentNumber,
        u.introduction,
        u.status
    )
    FROM User u
    WHERE u.id = :id
    """)
    Optional<UserProfile> findUserProfileById(@Param("id") Long userId);
}
