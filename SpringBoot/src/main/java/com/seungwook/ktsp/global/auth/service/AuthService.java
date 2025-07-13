package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.exception.DuplicatedException;
import com.seungwook.ktsp.global.auth.exception.LoginFailedException;
import com.seungwook.ktsp.global.auth.exception.StudentNumberException;
import com.seungwook.ktsp.global.auth.exception.VerifyCodeException;
import com.seungwook.ktsp.global.auth.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    //학번 형식
    private static final String STUDENT_NUMBER_PATTERN = "^[0-9]{9}$";

    public void register(RegisterRequest request) {

        // 학번 유효성 검사
        validStudentNumber(request.getStudentNumber());

        // 이메일, 학번 중복 검사
        validateDuplicateEmailAndStudent(request.getEmail(), request.getStudentNumber());

        User newUser = User.createUser(request.getEmail() + "@kangwon.ac.kr",
                passwordEncoder.encode(request.getPassword()),
                request.getStudentNumber(),
                request.getName(),
                request.getAcademicYear(),
                request.getTelNumber(),
                request.getMajor(),
                request.getPreviousGpa());

        userRepository.save(newUser);
    }


    public void login(LoginRequest request, HttpServletRequest httpRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getStudentNumber(), request.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증 정보 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 세션에 SecurityContext 저장
            HttpSession session = httpRequest.getSession(true); // 세션이 없으면 새로 생성
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        } catch (AuthenticationException ex) {
            throw new LoginFailedException();
        }
    }

    public void sendRegisterEmail(String toEmail) {
        emailService.sendRegisterEmail(toEmail, generateVerifyCode());
    }

    // 인증코드 검증
    public void verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);

        if (codeFoundByEmail == null) {
            throw new VerifyCodeException("인증번호 발송을 먼저 요청하세요");
        }

        if (!codeFoundByEmail.equals(code)) {
            log.warn("인증코드 검증 실패 - email: {}, 예상 값: {}, 입력값: {}", email, codeFoundByEmail, code);
            throw new VerifyCodeException("잘못된 인증번호입니다.");
        }
    }

    // 인증코드 생성
    public String generateVerifyCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private void validateDuplicateEmailAndStudent(String email, String studentNumber) {
        if (userRepository.existsByEmail(email)) throw new DuplicatedException("이미 등록된 이메일입니다.");
        if(userRepository.existsByStudentNumber(studentNumber)) throw new DuplicatedException("이미 등록된 학번입니다.");
    }

    // 학번 유효성 검사
    private void validStudentNumber(String studentNumber) {
        if (!Pattern.matches(STUDENT_NUMBER_PATTERN, studentNumber)) throw new StudentNumberException();

        int admissionYear = Integer.parseInt(studentNumber.substring(0, 4)); // 학번의 첫 4자리 (입학년도)
        int currentYear = Year.now().getValue(); // 현재 년도

        // 입학년도가 현재 년도보다 크면 안 됨
        if (admissionYear > currentYear) throw new StudentNumberException();

    }
}
