package com.seungwook.ktsp.domain.user.controller;

import com.seungwook.ktsp.domain.user.dto.request.MyInfoUpdateRequest;
import com.seungwook.ktsp.domain.user.dto.request.PasswordUpdateRequest;
import com.seungwook.ktsp.domain.user.dto.response.MyInfoResponse;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.mapper.UserResponseMapper;
import com.seungwook.ktsp.domain.user.service.UserService;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service/user")
public class MyInfoController {

    private final AuthService authService;
    private final UserService userService;

    // 내 정보 조회
    @GetMapping
    public ResponseEntity<Response<MyInfoResponse>> getMyInfo() {

        User user = userService.getUserInformation(authService.getUserId());
        MyInfoResponse response = UserResponseMapper.toMyinfoResponse(user);

        return ResponseEntity.ok(Response.<MyInfoResponse>builder()
                .message("내 정보를 불러왔습니다.")
                .data(response)
                .build());
    }

    // 내 정보 수정
    @PatchMapping
    public ResponseEntity<Response<MyInfoResponse>> updateMyInfo(@Valid @RequestBody MyInfoUpdateRequest request) {

        User user = userService.updateUserInformation(authService.getUserId(), request);
        MyInfoResponse response = UserResponseMapper.toMyinfoResponse(user);

        return ResponseEntity.ok(Response.<MyInfoResponse>builder()
                .message("내 정보를 수정했습니다.")
                .data(response)
                .build());
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<Response<Void>> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {

        userService.updatePassword(authService.getUserId(), request.getPassword());

        return ResponseEntity.ok(Response.<Void>builder()
                .message("비밀번호가 변경되었습니다.")
                .build());
    }
}
