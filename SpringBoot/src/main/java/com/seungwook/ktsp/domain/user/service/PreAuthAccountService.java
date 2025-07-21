package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.dto.request.PasswordResetRequest;
import com.seungwook.ktsp.domain.user.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.exception.RegisterFailedException;
import com.seungwook.ktsp.global.auth.exception.StudentNumberException;
import com.seungwook.ktsp.global.auth.exception.EmailVerifyException;
import com.seungwook.ktsp.global.auth.service.email.AuthCodeRedisService;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreAuthAccountService {

    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final AuthCodeRedisService authCodeRedisService;

    // 회원가입
    @Transactional
    public void register(RegisterRequest request, HttpServletRequest httpRequest) {

        // 강원대학교 이메일인지 검사
        checkEmailDomain(request.getEmail());

        // 이메일 인증 완료여부 검사
        isVerifyEmail(request.getEmail());

        // 학번 유효성 검사
        validStudentNumber(request.getStudentNumber());

        // 이메일, 학번, 전화번호 중복 검사
        validateDuplicate(request.getEmail(), request.getStudentNumber(), request.getPhoneNumber());

        // 유저 생성 및 저장
        User newUser = createUser(request);
        userDomainService.save(newUser);

        // 인증 완료 이메일 제거
        deleteVerifiedEmailFromRedis(request.getEmail());

        log.info("회원가입 성공 - userId: {}({})", newUser.getId(), IpUtil.getClientIP(httpRequest));
    }

    // 비밀번호 찾기
    @Transactional
    public void resetPassword(PasswordResetRequest request, HttpServletRequest httpRequest) {

        // 강원대학교 이메일인지 검사
        checkEmailDomain(request.getEmail());

        // 이메일 인증 완료여부 검사
        isVerifyEmail(request.getEmail());

        // 회원 조회
        User user = userDomainService.findByEmailExceptWithdrawn(request.getEmail());

        // 비밀번호 변경
        user.changePassword(passwordEncoder.encode(request.getPassword()));

        // 인증 완료 이메일 제거
        deleteVerifiedEmailFromRedis(request.getEmail());

        log.info("비밀번호 초기화 성공 - userId: {}({})", user.getId(), IpUtil.getClientIP(httpRequest));
    }

    // 학번 유효성 검사
    private void validStudentNumber(String studentNumber) {
        int admissionYear = Integer.parseInt(studentNumber.substring(0, 4)); // 학번의 첫 4자리 (입학년도)
        int currentYear = Year.now().getValue(); // 현재 년도
        if (admissionYear > currentYear) throw new StudentNumberException(); // 입학년도가 현재 년도보다 크면 안 됨
    }

    // 이메일, 학번, 전화번호 중복 검사
    private void validateDuplicate(String email, String studentNumber, String phoneNumber) {
        if (userDomainService.existsByEmail(email)) throw new RegisterFailedException("이미 등록된 이메일입니다.");
        if(userDomainService.existsByStudentNumber(studentNumber)) throw new RegisterFailedException("이미 등록된 학번입니다.");
        if(userDomainService.existsByPhoneNumber(phoneNumber)) throw new RegisterFailedException("이미 등록된 전화번호입니다.");
    }

    // User 객체 생성
    private User createUser(RegisterRequest request) {
        return User.createUser(request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getStudentNumber(),
                request.getName(),
                request.getAcademicYear(),
                request.getCampus(),
                request.getPhoneNumber(),
                request.getMajor(),
                request.getPreviousGpa());
    }

    // 강원대학교 이메일인지 검증
    private void checkEmailDomain(String email) {
        if (!email.endsWith("@kangwon.ac.kr"))
            throw new EmailVerifyException(HttpStatus.BAD_REQUEST, "강원대학교 이메일이 아닙니다.");
    }

    // 인증된 이메일인지 확인
    private void isVerifyEmail(String email) {
        if (!authCodeRedisService.existVerifiedEmail(email))
            throw new EmailVerifyException(HttpStatus.CONFLICT, "이메일 인증이 완료되지 않았습니다.");
    }

    // 인증 완료 이메일 레디스에서 제거
    private void deleteVerifiedEmailFromRedis(String email) {
        authCodeRedisService.deleteVerifiedEmail(email);
    }
}
