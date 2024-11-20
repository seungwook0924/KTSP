package com.zeroone.kstp.config;

import com.zeroone.kstp.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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
}