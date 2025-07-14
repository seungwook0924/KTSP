package com.seungwook.ktsp.global.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 회원가입 인증코드 이메일 발송
    public void sendRegisterEmail(String toEmail, String authCode) {
        try {
            if (redisService.existAuthCode(toEmail)) redisService.deleteAuthCode(toEmail);

            // 이메일 폼 생성
            MimeMessage emailForm = createEmailForm(toEmail, authCode);

            // Redis 에 인증 코드 저장(TTL: 5분)
            redisService.setAuthCode(toEmail, authCode, 60 * 5L);

            // 이메일 발송
            javaMailSender.send(emailForm);
            log.info("회원가입 인증코드 이메일 발송 성공 -  email: {}, authCode: {}", toEmail, authCode);
        }
        catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage(), e);
        }
    }

    // 비밀번호 찾기 인증코드 이메일 발송
    public void sendFindPasswordEmail(String toEmail, String tempPassword) throws MessagingException {
        try {
            // 이메일 폼 생성
            MimeMessage emailForm = createFindPasswordEmailForm(toEmail, tempPassword);

            // 이메일 발송
            javaMailSender.send(emailForm);
            log.info("비밀번호 찾기 인증코드 이메일 발송 성공 -  {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw e;
        }
    }

    // 이메일 내용 초기화
    private String setContext(String templateName, Map<String, Object> variables) {
        Context context = new Context();

        // 템플릿 변수 설정
        variables.forEach(context::setVariable);

        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("templates/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine.process(templateName, context);
    }

    // 회원가입용 이메일 폼 생성
    private MimeMessage createEmailForm(String email, String authCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("KNU 재능공유 플랫폼 인증번호");
            message.setFrom(senderEmail);

            // 템플릿 변수 설정
            Map<String, Object> variables = new HashMap<>();
            variables.put("code", authCode);

            message.setText(setContext("auth_code", variables), "utf-8", "html");
        }
        catch (MessagingException e) {
            log.error("Error while creating email form: {}", e.getMessage(), e);
            throw e;
        }
        return message;
    }

    // 비밀번호 찾기용 이메일 폼 생성
    private MimeMessage createFindPasswordEmailForm(String email, String tempPassword) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("KNU 재능공유 플랫폼 임시 비밀번호");
            message.setFrom(senderEmail);

            // 템플릿 변수 설정
            Map<String, Object> variables = new HashMap<>();
            variables.put("tempPassword", tempPassword);

            message.setText(setContext("temp_password", variables), "utf-8", "html");
        } catch (MessagingException e) {
            log.error("Error while creating email form: {}", e.getMessage(), e);
            throw e;
        }
        return message;
    }
}