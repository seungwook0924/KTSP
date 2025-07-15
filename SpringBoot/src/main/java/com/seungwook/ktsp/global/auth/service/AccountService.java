package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.exception.DuplicatedException;
import com.seungwook.ktsp.global.auth.exception.LoginFailedException;
import com.seungwook.ktsp.global.auth.exception.StudentNumberException;
import com.seungwook.ktsp.global.auth.exception.VerifyCodeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthCodeRedisService authCodeRedisService;

    // 학번 형식
    private static final String STUDENT_NUMBER_PATTERN = "^[0-9]{9}$";

    // 회원가입
    @Transactional
    public void register(RegisterRequest request) {

        String userEmail = request.getEmail() + "@kangwon.ac.kr";

        // 이메일 인증 완료여부 검사
        if (!authCodeRedisService.existVerifiedEmail(userEmail)){
            throw new VerifyCodeException(HttpStatus.CONFLICT, "이메일 인증이 완료되지 않았습니다.");
        }

        // 학번 유효성 검사
        validStudentNumber(request.getStudentNumber());

        // 이메일, 학번 중복 검사
        validateDuplicate(request.getEmail(), request.getStudentNumber(), request.getTelNumber());

        // 유저 생성 및 저장
        User newUser = User.createUser(userEmail,
                passwordEncoder.encode(request.getPassword()),
                request.getStudentNumber(),
                request.getName(),
                request.getAcademicYear(),
                request.getTelNumber(),
                request.getMajor(),
                request.getPreviousGpa());

        userRepository.save(newUser);

        // 인증 완료 이메일 제거
        authCodeRedisService.deleteVerifiedEmail(userEmail);

        log.info("회원가입 성공 - {}({})", newUser.getName(), newUser.getStudentNumber());
    }


    // 로그인
    @Transactional(readOnly = true)
    public void login(LoginRequest request, HttpServletRequest httpRequest) {
        // 사용자 조회
        User user = userRepository.findByStudentNumber(request.getStudentNumber()).orElseThrow(LoginFailedException::new);

        // 비밀번호 검사
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailedException();
        }

        // 정지된 계정인지 검사
        if (!user.getActivated()) {
            throw new LoginFailedException("정지된 계정입니다. 관리자에게 문의바랍니다.");
        }

        // 기존 세션이 있다면 제거
        HttpSession oldSession = httpRequest.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate(); // 세션 무효화
        }

        // 새로운 세션 생성
        HttpSession newSession = httpRequest.getSession(true);

        // 인증 토큰 구성
        UserSession sessionUser = new UserSession(
                user.getStudentNumber(),
                user.getRole()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                sessionUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        // SecurityContext 세팅
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // SecurityContext를 새로운 세션에 저장
        newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        log.info("로그인 성공 - {}({})", user.getName(), user.getStudentNumber());
    }

    // 로그아웃
    public void logout(HttpServletRequest request) {
        // 현재 SecurityContext 초기화
        SecurityContextHolder.clearContext();

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private void validateDuplicate(String email, String studentNumber, String telNumber) {
        if (userRepository.existsByEmail(email)) throw new DuplicatedException("이미 등록된 이메일입니다.");
        if(userRepository.existsByStudentNumber(studentNumber)) throw new DuplicatedException("이미 등록된 학번입니다.");
        if(userRepository.existsByTelNumber(telNumber)) throw new DuplicatedException("이미 등록된 전화번호입니다.");
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
