package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.UpdatePasswordDTO;
import com.zeroone.ktsp.DTO.UpdateUserDTO;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/update")
    public String showMypage(Model model, HttpSession session, UpdateUserDTO updateUserDTO)
    {
        if(session != null)
        {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
            updateUserDTO.setLevel(user.getLevel());
            updateUserDTO.setTel(user.getTel());
            updateUserDTO.setMajor(user.getMajor());
            updateUserDTO.setLastGrades(user.getLastGrades());
        }
        model.addAttribute("updateUserDTO", updateUserDTO);
        model.addAttribute("currentMenu", "one");
        return "my_page/update";
    }

    @PostMapping("/update")
    public String updateUser(HttpSession session, @ModelAttribute UpdateUserDTO updateUserDTO)
    {
        if(session != null)
        {
            User user = (User)session.getAttribute("user");
            User updateUser = user.toBuilder()
                    .level(updateUserDTO.getLevel())
                    .tel(updateUserDTO.getTel())
                    .major(updateUserDTO.getMajor())
                    .lastGrades(updateUserDTO.getLastGrades())
                    .build();
            session.setAttribute("user", updateUser);
            userService.save(updateUser);
        }
        return "redirect:/mypage";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(HttpSession session, @ModelAttribute UpdatePasswordDTO updatePasswordDTO)
    {
        if(session != null)
        {
            User user = (User)session.getAttribute("user");
            if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())) {
                // 비밀번호 불일치 처리
                return "redirect:/mypage?error=비밀번호가 일치하지 않습니다.";
            }

            // 새 비밀번호를 BCrypt로 인코딩
            String encodedPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());

            User updateUser = user.toBuilder()
                    .password(encodedPassword)
                    .build();
            session.setAttribute("user", updateUser);
            log.info("{}", updatePasswordDTO.getNewPassword());
            userService.save(updateUser);
        }
        return "redirect:/mypage";
    }
}
