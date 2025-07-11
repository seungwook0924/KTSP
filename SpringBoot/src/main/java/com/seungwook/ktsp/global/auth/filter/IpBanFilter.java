package com.seungwook.ktsp.global.auth.filter;

import com.seungwook.ktsp.global.api.service.ApiService;
import com.seungwook.ktsp.global.auth.handler.BannedIpHandler;
import com.seungwook.ktsp.global.auth.service.IpService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpBanFilter extends OncePerRequestFilter {

    private final IpService ipService;
    private final ApiService apiService;
    private final BannedIpHandler bannedIpHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String clientIP = ipService.getClientIP(request);

        if(apiService.isSafeIp(clientIP)) filterChain.doFilter(request, response);

        bannedIpHandler.handle(response);
    }
}