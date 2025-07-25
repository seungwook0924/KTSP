package com.seungwook.ktsp.global.common.support;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.entity.enums.AcademicYear;
import com.seungwook.ktsp.domain.user.entity.enums.Campus;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserInitializer {

    private final UserDomainService userDomainService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

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

    @Transactional
    @PostConstruct
    public void initUser() {

        // admin 생성
        if ((!userDomainService.existsByEmail(email)) && (!userDomainService.existsByStudentNumber(studentNumber))) {
            User admin = User.createAdmin(email, password, studentNumber, name, phoneNumber, major);
            userDomainService.save(admin);
        }

        // test용 더미 유저
        if (!activeProfile.equals("prod")) {
            String email = "test@kangwon.ac.kr";
            String studentNumber = "201921322";
            String phoneNumber = "010-1234-5679";

            if (!userDomainService.existsByEmail(email) && !userDomainService.existsByStudentNumber(studentNumber)) {
                User user = User.createUser(email,
                        password,
                        studentNumber,
                        "홍길동",
                        AcademicYear.FIRST_YEAR,
                        Campus.SAMCHEOK,
                        phoneNumber,
                        "컴퓨터공학과"
                        , new BigDecimal("3.2"));
                userDomainService.save(user);
            }
        }
    }
}
