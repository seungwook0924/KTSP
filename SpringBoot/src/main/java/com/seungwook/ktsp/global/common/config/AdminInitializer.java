package com.seungwook.ktsp.global.common.config;

import com.seungwook.ktsp.domain.service.user.entity.User;
import com.seungwook.ktsp.domain.service.user.repository.UserRepository;
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

    @Value("${admin.tel}")
    private String tel;

    @Value("${admin.major}")
    private String major;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByEmail(email)) {
            User admin = User.createAdmin(email, password, studentNumber, name, tel, major);
            userRepository.save(admin);
        }
    }
}
