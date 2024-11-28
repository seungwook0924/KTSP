package com.zeroone.ktsp.util;

import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.UserLevel;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class MethodUtil
{
    public User getSessionUser(HttpSession session)
    {
        return (User)session.getAttribute("user");
    }

    // 유효한 SidebarType과 CurrentMenu 확인
    public boolean isValidSidebarTypeAndMenu(String sidebarType, String currentMenu)
    {
        Set<String> validSidebarTypes = Set.of("learningCore", "majorLearner", "projectContest", "community");
        Set<String> validMenus = Set.of("mentor", "mentee", "major1", "major2", "major3", "projectContest", "notice", "report");

        return validSidebarTypes.contains(sidebarType) && validMenus.contains(currentMenu);
    }

    public String convertUserLevel(UserLevel level)
    {
        switch (level)
        {
            case freshman: return "1학년";
            case sophomore: return "2학년";
            case junior: return "3학년";
            case senior: return "4학년";
            default: return "기타";
        }
    }
}
