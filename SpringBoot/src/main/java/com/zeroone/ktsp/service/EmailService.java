package com.zeroone.ktsp.service;

import com.zeroone.ktsp.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private static final String senderEmail = "rebugs0924@gmail.com";

    // 인증코드 이메일 발송
    public void sendEmail(String toEmail) throws MessagingException
    {
        log.info("서비스 계층 sendEmail() - send email to: {}", toEmail); // 로그 추가
        try
        {
            if (redisUtil.existData(toEmail)) redisUtil.deleteData(toEmail);

            // 이메일 폼 생성
            MimeMessage emailForm = createEmailForm(toEmail);

            // 이메일 발송
            javaMailSender.send(emailForm);
        }
        catch (MessagingException e)
        {
            log.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage(), e); // 에러 로그 추가
            throw e;
        }
    }

    // 아이디 찾기 이메일 발송
    public void sendFindEmail(String toEmail, String tempPassword) throws MessagingException
    {
        log.info("서비스 계층 sendFindEmail() - send email to: {}", toEmail); // 로그 추가

        try {
            // 이메일 폼 생성
            MimeMessage emailForm = createFindPasswordEmailForm(toEmail, tempPassword);

            // 이메일 발송
            javaMailSender.send(emailForm);
            log.info("Email sent successfully to: {}", toEmail); // 로그 추가
        } catch (MessagingException e)
        {
            log.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage(), e); // 에러 로그 추가
            throw e;
        }
    }

    // 코드 검증
    public Boolean verifyEmailCode(String email, String code)
    {
        String codeFoundByEmail = redisUtil.getData(email);
        log.info("Verification attempt for email: {}. Code found in Redis: {}", email, codeFoundByEmail); // 로그 추가

        if (codeFoundByEmail == null)
        {
            log.warn("No code found for email: {}", email); // 경고 로그 추가
            return false;
        }

        boolean result = codeFoundByEmail.equals(code);
        log.info("Verification result for email: {} is {}", email, result ? "successful" : "failed"); // 로그 추가
        return result;
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

    // 이메일 내용 초기화
    private String setContext(String templateName, Map<String, Object> variables)
    {
        log.debug("Initializing email context for template: {}", templateName); // 로그 추가
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
    private MimeMessage createEmailForm(String email) throws MessagingException
    {
        String authCode = createCode();
        log.info("Generated auth code: {}", authCode);

        MimeMessage message = javaMailSender.createMimeMessage();
        try
        {
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("KNU 재능공유 플랫폼 인증번호");
            message.setFrom(senderEmail);

            // 템플릿 변수 설정
            Map<String, Object> variables = new HashMap<>();
            variables.put("code", authCode);

            message.setText(setContext("auth_code", variables), "utf-8", "html");

            // Redis 에 인증 코드 저장
            redisUtil.setDataExpire(email, authCode, 60 * 30L);
        }
        catch (MessagingException e)
        {
            log.error("Error while creating email form: {}", e.getMessage(), e);
            throw e;
        }
        return message;
    }

    // 비밀번호 찾기용 이메일 폼 생성
    private MimeMessage createFindPasswordEmailForm(String email, String tempPassword) throws MessagingException {
        log.info("Generated temporary password: {}", tempPassword);

        MimeMessage message = javaMailSender.createMimeMessage();
        try
        {
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("KNU 재능공유 플랫폼 임시 비밀번호");
            message.setFrom(senderEmail);

            // 템플릿 변수 설정
            Map<String, Object> variables = new HashMap<>();
            variables.put("tempPassword", tempPassword);

            message.setText(setContext("temp_password", variables), "utf-8", "html");
        }
        catch (MessagingException e)
        {
            log.error("Error while creating email form: {}", e.getMessage(), e);
            throw e;
        }
        return message;
    }
}
