package com.seungwook.ktsp.domain.user.entity;

import com.seungwook.ktsp.domain.user.entity.enums.AcademicYear;
import com.seungwook.ktsp.domain.user.entity.enums.Campus;
import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 13)
    private String phoneNumber;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Column(name = "student_number", unique = true, length = 9)
    private String studentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_year", nullable = false)
    private AcademicYear academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "campus", nullable = false)
    private Campus campus;

    @Column(name = "major", nullable = false, length = 20)
    private String major;

    @Column(name = "previous_gpa", precision = 3, scale = 2, nullable = false)
    private BigDecimal previousGpa;

    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password, String studentNumber, String name, AcademicYear academicYear, Campus campus, String phoneNumber, String major, BigDecimal previousGpa, UserRole role) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.academicYear = academicYear;
        this.studentNumber = studentNumber;
        this.campus = campus;
        this.major = major;
        this.previousGpa = previousGpa;
        this.activated = true;
        this.role = role;
    }

    // 일반 유저 생성
    public static User createUser(String email, String encodedPassword, String studentNumber, String name, AcademicYear academicYear, Campus campus, String phoneNumber, String major, BigDecimal previousGpa) {
        return User.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .studentNumber(studentNumber)
                .password(encodedPassword)
                .name(name)
                .academicYear(academicYear)
                .campus(campus)
                .major(major)
                .previousGpa(previousGpa)
                .role(UserRole.USER)
                .build();
    }

    // 어드민 생성
    public static User createAdmin(String email, String encodedPassword, String studentNumber, String name, String telNumber, String major) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .phoneNumber(telNumber)
                .name(name)
                .studentNumber(studentNumber)
                .academicYear(AcademicYear.GRADUATE)
                .campus(Campus.CHUNCHEON)
                .major(major)
                .previousGpa(new BigDecimal("4.0"))
                .role(UserRole.ADMIN)
                .build();
    }

    // 비밀번호 변경
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    // 학년 변경
    public void changeUserInformation(AcademicYear newYear, String newPhoneNumber, String newMajor, BigDecimal newGpa, Campus newCampus) {
        this.academicYear = newYear;
        this.phoneNumber = newPhoneNumber;
        this.major = newMajor;
        this.previousGpa = newGpa;
        this.campus = newCampus;
    }

    // 계정 비활성화
    public void deactivate() {
        this.activated = false;
    }

    // 계정 활성화
    public void activate() {
        this.activated = true;
    }
}
