package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.DTO.UpdateUserDTO;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.service.UserService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MethodUtil methodUtil;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^010-\\d{4}-\\d{4}$");

    @GetMapping("/update")
    public String showMypage(Model model, UpdateUserDTO updateUserDTO, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);

        model.addAttribute("user", user);
        updateUserDTO.setLevel(user.getLevel());
        updateUserDTO.setTel(user.getTel());
        updateUserDTO.setMajor(user.getMajor());
        updateUserDTO.setLastGrades(user.getLastGrades().toString());

        model.addAttribute("updateUserDTO", updateUserDTO);
        model.addAttribute("currentMenu", "one");
        return "my_page/update";
    }

    @PostMapping("/update")
    public String updateUser(@Validated @ModelAttribute UpdateUserDTO updateUserDTO, RedirectAttributes redirectAttributes, HttpSession session)
    {
        // 입력 데이터 유효성 검사
        String validationError = validateUserData(updateUserDTO);
        if (validationError != null)
        {
            redirectAttributes.addFlashAttribute("errorMessage", validationError);
            return "redirect:/mypage/update";
        }

        User user = methodUtil.getSessionUser(session);
        User updateUser = user.toBuilder()
                .level(updateUserDTO.getLevel())
                .tel(updateUserDTO.getTel())
                .major(updateUserDTO.getMajor())
                .lastGrades(new BigDecimal(updateUserDTO.getLastGrades()))
                .build();

        // 업데이트된 사용자 정보 저장
        userService.save(updateUser);
        session.setAttribute("user", updateUser);

        log.info("사용자 정보 업데이트 - 이름 : {} / 학번 : {}", user.getName(), user.getStudentNumber());
        return "redirect:/mypage";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute UpdateUserDTO updateUserDTO, RedirectAttributes redirectAttributes, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);

        if (!updateUserDTO.getNewPassword().equals(updateUserDTO.getConfirmPassword()))
        {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage";
        }

        String encodedPassword = passwordEncoder.encode(updateUserDTO.getNewPassword()); // 암호화

        User updateUser = user.toBuilder()
                .password(encodedPassword)
                .build();

        userService.save(updateUser);
        session.setAttribute("user", updateUser);

        log.info("비밀번호 변경 - 이름 : {} / 학번 : {}", user.getName(), user.getStudentNumber());
        return "redirect:/mypage";
    }

    private String validateUserData(UpdateUserDTO updateUserDTO)
    {
        // 전공 필드 유효성 검사
        if (updateUserDTO.getMajor() == null || updateUserDTO.getMajor().trim().isEmpty()) return "전공을 입력해주세요.";
        if (updateUserDTO.getMajor().length() < 2 || updateUserDTO.getMajor().length() > 20) return "전공은 2글자 이상 20글자 이하로 입력해야 합니다.";

        // 전화번호 형식 검사
        if (!PHONE_PATTERN.matcher(updateUserDTO.getTel()).matches()) return "전화번호는 010-XXXX-XXXX 형식이어야 합니다.";

        // 학점 유효성 검사
        try
        {
            BigDecimal grades = new BigDecimal(updateUserDTO.getLastGrades());

            if (grades.scale() > 2) return "학점은 소수점 아래 두 자리까지만 입력 가능합니다.";

            BigDecimal minGrade = new BigDecimal("0.0");
            BigDecimal maxGrade = new BigDecimal("4.5");

            if (grades.compareTo(minGrade) < 0 || grades.compareTo(maxGrade) > 0) return "학점은 0.0 이상 4.5 이하여야 합니다.";
        }
        catch (NumberFormatException e)
        {
            return "학점은 숫자 형식이어야 합니다.";
        }

        return null;
    }
}