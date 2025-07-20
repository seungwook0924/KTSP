package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.user.dto.request.UserInfoUpdateRequest;
import com.seungwook.ktsp.domain.user.dto.request.PasswordUpdateRequest;
import com.seungwook.ktsp.domain.user.dto.request.WithdrawnRequest;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.exception.PasswordMismatchException;
import com.seungwook.ktsp.domain.user.exception.RememberMeAccessDeniedException;
import com.seungwook.ktsp.domain.user.exception.UserUpdateFailedException;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    // 내 정보 수정
    @Transactional
    public User updateUserInformation(long userId, UserInfoUpdateRequest request) {
        User user = findById(userId);

        String newPhoneNumber = request.getPhoneNumber();
        String oldPhoneNumber = user.getPhoneNumber();
        if (!newPhoneNumber.equals(oldPhoneNumber))
            if(userDomainService.existsByPhoneNumber(newPhoneNumber))
                throw new UserUpdateFailedException(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다.");


        user.changeUserInformation(request.getAcademicYear(),
                request.getPhoneNumber(),
                request.getMajor(),
                request.getPreviousGpa(),
                request.getCampus(),
                request.getIntroduction());

        log.info("회원정보 변경 성공 - userId: {}", user.getId());

        return user;
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(long userId, PasswordUpdateRequest request){
        User user = findById(userId);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new UserUpdateFailedException(HttpStatus.UNAUTHORIZED, "기존 비밀번호가 일치하지 않습니다.");


        // 암호화 저장
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));

        log.info("비밀번호 변경 성공 - userId: {}", user.getId());
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawnUser(UserSession userSession, WithdrawnRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        // 자동 로그인 상태에선 요청 거절
        if(userSession.isRememberMe()) throw new RememberMeAccessDeniedException();

        long userId = userSession.getId();
        User user = findById(userId);

        // 비밀번호 검사
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new PasswordMismatchException();

        // 로그아웃
        authService.logout(httpRequest, httpResponse);

        // 탈퇴(soft delete)
        userDomainService.delete(user);

        log.info("회원 탈퇴 완료: {}({})", userId, IpUtil.getClientIP(httpRequest));
    }

    // 활성화된 회원 조회
    private User findById(long userId) {
        return userDomainService.findActiveUserById(userId);
    }
}
