package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @ModelAttribute("loggedInUser")
    public User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        // 요청 경로를 확인하여 동적 리다이렉트
        String referer = request.getHeader("Referer");
        log.info("{}", referer);

        // 사용자에게 보낼 에러 메시지 설정
        redirectAttributes.addFlashAttribute("errorMessage", "파일 크기가 최대 허용 용량(50MB)을 초과했습니다.");

        // Referer 헤더가 없다면 기본 경로로 리다이렉트
        if (referer == null || referer.isEmpty()) {
            return "redirect:/default-error-page";
        }

        return "redirect:" + referer; // 발생한 요청 경로로 리다이렉트
    }
}