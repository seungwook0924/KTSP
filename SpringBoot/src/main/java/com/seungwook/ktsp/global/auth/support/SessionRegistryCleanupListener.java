package com.seungwook.ktsp.global.auth.support;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.events.AbstractSessionEvent;
import org.springframework.stereotype.Component;

// Redis에 저장된 세션이 만료돼도 Registry에 엔트리가 남지 않도록 설정
@Component
@RequiredArgsConstructor
public class SessionRegistryCleanupListener implements ApplicationListener<AbstractSessionEvent> {

    private final SessionRegistry sessionRegistry;

    @Override
    public void onApplicationEvent(AbstractSessionEvent event) {
        sessionRegistry.removeSessionInformation(event.getSessionId());
    }
}
