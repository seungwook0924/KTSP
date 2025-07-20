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

    //
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByStudentNumber(String studentNumber) {
        return userRepository.existsByStudentNumber(studentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByStudentNumberExceptWithdrawn(String studentNumber) {
        return userRepository.findByStudentNumberExceptWithdrawn(studentNumber);
    }

    @Transactional(readOnly = true)
    public User findByEmailExceptWithdrawn(String email) {
        return userRepository.findByEmailExceptWithdrawn(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User findByIdExceptWithdrawn(Long userId) {
        return userRepository.findByIdExceptWithdrawn(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public UserProfile findUserProfileById(Long userId) {
        return userRepository.findUserProfileById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> findActiveById(Long userId) {
        return userRepository.findByIdExceptWithdrawn(userId)
                .filter(User::isActive);
    }
}
