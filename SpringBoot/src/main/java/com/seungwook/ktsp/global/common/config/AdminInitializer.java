package com.seungwook.ktsp.global.common.config;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.student-number}")
    private String studentNumber;

    @Value("${admin.name}")
    private String name;

    @Value("${admin.phoneNumber}")
    private String phoneNumber;

    @Value("${admin.major}")
    private String major;

    @PostConstruct
    public void initAdmin() {
        if ((!userRepository.existsByEmail(email)) && (!userRepository.existsByStudentNumber(studentNumber))) {
            User admin = User.createAdmin(email, password, studentNumber, name, phoneNumber, major);
            userRepository.save(admin);
        }
    }
}
