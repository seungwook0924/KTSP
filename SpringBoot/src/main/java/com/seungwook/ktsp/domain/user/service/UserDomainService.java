package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.exception.UserNotFoundException;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    // User 저장
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    // User 삭제
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    // 프록시 객체 반환(성능 최적화)
    public User getReferenceById(long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException();

        return userRepository.getReferenceById(userId);
    }

    // 이미 등록된 이메일인지 검사
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 이미 등록된 학번인지 검사
    @Transactional(readOnly = true)
    public boolean existsByStudentNumber(String studentNumber) {
        return userRepository.existsByStudentNumber(studentNumber);
    }

    // 이미 등록된 전화번호인지 검사
    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    // 학번을 기준으로 탈퇴하지 않은 User 리턴
    @Transactional(readOnly = true)
    public Optional<User> findByStudentNumberExceptWithdrawn(String studentNumber) {
        return userRepository.findByStudentNumberExceptWithdrawn(studentNumber);
    }

    // 이메일을 기준으로 탈퇴하지 않은 User 리턴
    @Transactional(readOnly = true)
    public User findByEmailExceptWithdrawn(String email) {
        return userRepository.findByEmailExceptWithdrawn(email)
                .orElseThrow(() -> new UserNotFoundException("탈퇴했거나 존재하지 않는 사용자입니다."));
    }

    // userId를 바탕으로 활성화된 User 리턴
    @Transactional(readOnly = true)
    public User findActiveUserById(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("활성화된 회원이 아니거나 사용자를 찾을 수 없습니다."));
    }

    // userId를 바탕으로 Optional<User> 리턴
    @Transactional(readOnly = true)
    public Optional<User> findActiveById(Long userId) {
        return userRepository.findActiveUserById(userId);
    }

    // userId를 바탕으로 탈퇴하지 않은 User 리턴
    @Transactional(readOnly = true)
    public User findByIdExceptWithdrawn(long userId) {
        return userRepository.findByIdExceptWithdrawn(userId)
                .orElseThrow(() -> new UserNotFoundException("탈퇴했거나 존재하지 않는 사용자입니다."));
    }

    // userId를 바탕으로 UserProfile 리턴
    @Transactional(readOnly = true)
    public UserProfile findUserProfileById(Long userId) {
        return userRepository.findUserProfileById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
