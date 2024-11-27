package com.zeroone.ktsp.util;

import com.zeroone.ktsp.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class MethodUtil
{
    public User getSessionUser(HttpSession session)
    {
        return (User)session.getAttribute("user");
    }
}
