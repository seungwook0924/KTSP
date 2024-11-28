package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.DTO.login.RegisterDTO;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.service.EmailService;
import com.zeroone.ktsp.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    //학번 형식
    private static final String STUDENT_NUMBER_PATTERN = "^[0-9]{9}$";

    @PostMapping("/register")
    public String registerUser(@Validated @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        registerDTO.setEmail(registerDTO.getEmail() + "@kangwon.ac.kr"); //@kangwon.ac.kr 붙혀서 등록
        if (bindingResult.hasErrors()) return "login/register";

        if (userService.findEMail(registerDTO.getEmail()).isPresent())
        {
            bindingResult.rejectValue("email", "error.studentNumber", "이미 등록된 이메일입니다.");
            return "login/register";
        }

        if (userService.isStudentNumberExists(registerDTO.getStudentNumber()))
        {
            bindingResult.rejectValue("studentNumber", "error.studentNumber", "이미 존재하는 학번입니다.");
            return "login/register";
        }

        if(!isValidStudentNumber(registerDTO.getStudentNumber()))
        {
            bindingResult.rejectValue("studentNumber", "error.studentNumber", "유효하지 않은 학번입니다.");
            return "login/register";
        }

        userService.registerUser(registerDTO);
        log.info("회원가입 성공 - 이름 : {} / 학번 : {}", registerDTO.getName(), registerDTO.getStudentNumber());

        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:/login";
    }

    @GetMapping("/findpassword")
    public String showFindPassword()
    {
        return "login/find_password";
    }

    @PostMapping("/findpassword")
    public String findPassword(@RequestParam String studentNumber, RedirectAttributes redirectAttributes) throws MessagingException
    {
        Optional<User> user = userService.findStudentNumber(studentNumber);
        if(user.isPresent())
        {
            String newPassword = createCode();
            String encodedPassword = passwordEncoder.encode(newPassword); // 새 비밀번호를 BCrypt로 인코딩

            User updateUser = user.get().toBuilder()
                    .password(encodedPassword)
                    .build();
            userService.save(updateUser);

            String targetEmail = updateUser.getEMail();
            emailService.sendFindEmail(targetEmail, newPassword);

            log.info("비밀번호 변경 요청 : {}", studentNumber);
            redirectAttributes.addFlashAttribute("message", "등록된 이메일로 임시 비밀번호가 전송되었습니다.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "등록된 아이디가 없습니다.");
        return "redirect:/login/findpassword";
    }

    private String createCode()
    {
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

    // 학번 유효성 검사
    public static boolean isValidStudentNumber(String studentNumber)
    {
        if (!Pattern.matches(STUDENT_NUMBER_PATTERN, studentNumber)) return false; // 9자리 숫자가 아니면 유효하지 않음

        // 학번의 첫 4자리 (입학년도)
        int admissionYear = Integer.parseInt(studentNumber.substring(0, 4));

        // 현재 년도와 비교
        int currentYear = Year.now().getValue();  // 현재 년도

        // 입학년도가 현재 년도보다 크면 안 되고, 10년 이전이면 안 됨
        if (admissionYear > currentYear || admissionYear < (currentYear - 10)) return false; // 유효하지 않은 학번

        return true; // 유효한 학번
    }
}
