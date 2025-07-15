package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.exception.RegisterFailedException;
import com.seungwook.ktsp.global.auth.exception.StudentNumberException;
import com.seungwook.ktsp.global.auth.exception.EmailVerifyException;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthCodeRedisService authCodeRedisService;

    // 학번 형식
    private static final String STUDENT_NUMBER_PATTERN = "^[0-9]{9}$";

    // 회원가입
    @Transactional
    public void register(RegisterRequest request, HttpServletRequest httpRequest) {

        if (!request.getEmail().endsWith("@kangwon.ac.kr"))
            throw new RegisterFailedException(HttpStatus.BAD_REQUEST, "강원대학교 이메일이 아닙니다.");

        // 이메일 인증 완료여부 검사
        if (!authCodeRedisService.existVerifiedEmail(request.getEmail()))
            throw new EmailVerifyException(HttpStatus.CONFLICT, "이메일 인증이 완료되지 않았습니다.");

        // 학번 유효성 검사
        validStudentNumber(request.getStudentNumber());

        // 이메일, 학번, 전화번호 중복 검사
        validateDuplicate(request.getEmail(), request.getStudentNumber(), request.getPhoneNumber());

        // 유저 생성 및 저장
        User newUser = createUser(request);
        userRepository.save(newUser);

        // 인증 완료 이메일 제거
        authCodeRedisService.deleteVerifiedEmail(request.getEmail());

        log.info("회원가입 성공 - {}({} / {})", newUser.getName(), newUser.getStudentNumber(), IpUtil.getClientIP(httpRequest));
    }

    // 학번 유효성 검사
    private void validStudentNumber(String studentNumber) {
        if (!Pattern.matches(STUDENT_NUMBER_PATTERN, studentNumber)) throw new StudentNumberException();

        int admissionYear = Integer.parseInt(studentNumber.substring(0, 4)); // 학번의 첫 4자리 (입학년도)
        int currentYear = Year.now().getValue(); // 현재 년도

        // 입학년도가 현재 년도보다 크면 안 됨
        if (admissionYear > currentYear) throw new StudentNumberException();

    }

    // 이메일, 학번, 전화번호 중복 검사
    private void validateDuplicate(String email, String studentNumber, String telNumber) {
        if (userRepository.existsByEmail(email)) throw new RegisterFailedException("이미 등록된 이메일입니다.");
        if(userRepository.existsByStudentNumber(studentNumber)) throw new RegisterFailedException("이미 등록된 학번입니다.");
        if(userRepository.existsByPhoneNumber(telNumber)) throw new RegisterFailedException("이미 등록된 전화번호입니다.");
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
}
