package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.DTO.login.LoginDTO;
import com.zeroone.ktsp.DTO.login.RegisterDTO;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController
{
    private final MethodUtil methodUtil;
    @GetMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/learning_core")
    public String learningCore(Model model)
    {
        model.addAttribute("currentMenu", "description");
        return "main_pages/learning_core";
    }

    @GetMapping("/major_learner")
    public String majorLearner(Model model)
    {
        model.addAttribute("currentMenu", "description");
        return "main_pages/major_learner";
    }

    @GetMapping("/project_contest")
    public String challengeLearner(Model model)
    {
        model.addAttribute("currentMenu", "description");
        return "main_pages/project_contest";
    }

    @GetMapping("/introduction")
    public String introduction(Model model)
    {
        model.addAttribute("currentMenu", "introduction");
        return "community/introduction";
    }

    @GetMapping("/login")
    public String showLogin(Model model)
    {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login/login";
    }

    @GetMapping("/register")
    public String showRegister(Model model)
    {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "login/register";
    }

    @GetMapping("/mypage")
    public String showMypage(Model model, HttpSession session)
    {
        if(session != null)
        {
            User user = (User)session.getAttribute("user");
            String levelText = methodUtil.convertUserLevelToString(user.getLevel());

            model.addAttribute("user", user);
            model.addAttribute("levelText", levelText);
        }
        model.addAttribute("currentMenu", "mypage_main");
        return "my_page/mypage";
    }
}
