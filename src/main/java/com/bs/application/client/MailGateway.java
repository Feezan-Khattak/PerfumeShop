package com.bs.application.client;

import com.bs.application.utils.AppProps;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailGateway {
    private final AppProps appProps;
    private final JavaMailSender mailSender;

    @Async
    public void sendEmailAsync(String recipient, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        log.info("Configuring Java Mail Sender");
        helper.setFrom(appProps.getSystemMailAddress());
        helper.setTo(recipient);
        helper.setReplyTo("noreply@gmail.com");
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(mimeMessage);
        log.info("Email Sent.");
    }
}
