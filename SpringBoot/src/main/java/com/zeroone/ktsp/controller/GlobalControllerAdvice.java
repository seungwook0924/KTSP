package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.UserRole;
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
    public boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("user") != null;
    }

    @ModelAttribute("loggedInUser")
    public User getLoggedInUser(HttpSession session)
    {
        return (User) session.getAttribute("user");
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if (user == null) return false;
        return user.getRole().equals(UserRole.admin);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(HttpServletRequest request, RedirectAttributes redirectAttributes, HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        log.warn("파일 업로드 최대크기 초과 - 유저 pk : {}, 이름 : {}", user.getId(), user.getName());
        String referer = request.getHeader("Referer");// 요청 경로를 확인하여 동적 리다이렉트
        redirectAttributes.addFlashAttribute("errorMessage", "파일 크기가 최대 허용 용량(50MB)을 초과했습니다.");
        return "redirect:" + referer; // 발생한 요청 경로로 리다이렉트
    }
}