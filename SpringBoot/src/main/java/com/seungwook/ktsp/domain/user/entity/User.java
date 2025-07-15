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

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "student_number", unique = true, length = 9)
    private String studentNumber;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_year", nullable = false)
    private AcademicYear academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "campus", nullable = false)
    private Campus campus;

    @Column(name = "phone_number", nullable = false, unique = true, length = 13)
    private String phoneNumber;

    @Column(name = "major", nullable = false, length = 20)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "previous_gpa", precision = 3, scale = 2, nullable = false)
    private BigDecimal previousGpa;

    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password, String studentNumber, String name, AcademicYear academicYear, Campus campus, String phoneNumber, String major, BigDecimal previousGpa, UserRole role) {
        this.email = email;
        this.password = password;
        this.studentNumber = studentNumber;
        this.name = name;
        this.academicYear = academicYear;
        this.campus = campus;
        this.phoneNumber = phoneNumber;
        this.major = major;
        this.role = role;
        this.previousGpa = previousGpa;
        this.activated = true;
    }

    // 일반 유저 생성
    public static User createUser(String email, String encodedPassword, String studentNumber, String name, AcademicYear academicYear, Campus campus, String phoneNumber, String major, BigDecimal previousGpa) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .studentNumber(studentNumber)
                .name(name)
                .academicYear(academicYear)
                .campus(campus)
                .phoneNumber(phoneNumber)
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
                .studentNumber(studentNumber)
                .name(name)
                .academicYear(AcademicYear.GRADUATE)
                .campus(Campus.CHUNCHEON)
                .phoneNumber(telNumber)
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
    public void changeAcademicYear(AcademicYear newYear) {
        this.academicYear = newYear;
    }

    // 전화번호 변경
    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // 전공 변경
    public void changeMajor(String newMajor) {
        this.major = newMajor;
    }

    // 이전학기 학점 변경
    public void changePreviousGpa(BigDecimal newGpa) {
        this.previousGpa = newGpa;
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
