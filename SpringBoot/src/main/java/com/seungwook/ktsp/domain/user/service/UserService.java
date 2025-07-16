package com.seungwook.ktsp.domain.user.service;

import com.seungwook.ktsp.domain.user.dto.request.MyInfoUpdateRequest;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.exception.UserUpdateFailedException;
import com.seungwook.ktsp.domain.user.exception.UserNotFoundException;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 내 정보 조회
    @Transactional(readOnly = true)
    public User getUserInformation(long userId) {
        return findById(userId);
    }

    // 내 정보 수정
    @Transactional
    public User updateUserInformation(long userId, MyInfoUpdateRequest request) {
        User user = findById(userId);

        String newPhoneNumber = request.getPhoneNumber();
        String oldPhoneNumber = user.getPhoneNumber();
        if (!newPhoneNumber.equals(oldPhoneNumber)) {
            if(userRepository.existsByPhoneNumber(newPhoneNumber))
                throw new UserUpdateFailedException(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다.");
        }

        user.changeUserInformation(request.getAcademicYear(),
                request.getPhoneNumber(),
                request.getMajor(),
                request.getPreviousGpa(),
                request.getCampus());

        return user;
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(long userId, String password){
        User user = findById(userId);

        // 암호화 저장
        user.changePassword(passwordEncoder.encode(password));
    }

    private User findById(long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
