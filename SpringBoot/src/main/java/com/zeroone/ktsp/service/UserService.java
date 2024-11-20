package com.zeroone.ktsp.service;

import com.zeroone.ktsp.DTO.RegisterDTO;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.UserRole;
import com.zeroone.ktsp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // PK로 유저 찾기
    public Optional<User> findUserById(Long id)
    {
        return userRepository.findById(id);
    }

    // 학번으로 유저 찾기
    public Optional<User> findStudentNumber(String studentNumber)
    {
        return userRepository.findByStudentNumber(studentNumber);
    }

    // 학번으로 유저 찾기
    public Optional<User> findEMail(String studentNumber)
    {
        return userRepository.findByEMail(studentNumber);
    }

    public void save(User user)
    {
        userRepository.save(user);
    }

    public void registerUser(RegisterDTO registerDTO) {
        User user = User.builder()
                .studentNumber(registerDTO.getStudentNumber())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .name(registerDTO.getName())
                .tel(registerDTO.getTel())
                .EMail(registerDTO.getEmail())
                .level(registerDTO.getLevel())
                .major(registerDTO.getMajor())
                .lastGrades(registerDTO.getLastGrade())
                .role(UserRole.user)
                .build();
        userRepository.save(user);
    }

    public boolean isStudentNumberExists(String studentNumber) {
        return userRepository.existsByStudentNumber(studentNumber);
    }
}
