package com.bs.application.services;

import com.bs.application.client.MailGateway;
import com.bs.application.dtos.MailRequest;
import com.bs.application.dtos.Response;
import com.bs.application.entities.Template;
import com.bs.application.repos.TemplateRepo;
import com.bs.application.utils.ResponseUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final MailGateway mailGateway;
    private final TemplateRepo templateRepo;
    private final ResponseUtil responseUtil;

    public Response sendEmail(MailRequest req) throws MessagingException {
        Template template = templateRepo.findByTemplateKey(req.getTemplate())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Template selected"));

        String templateString = template.getTemplate();
        log.info("Embedding template values...");

        for (String key : req.getData().keySet()) {
            templateString = templateString.replace("{{" + key + "}}", req.getData().get(key));
        }

        mailGateway.sendEmailAsync(req.getEmail(), req.getSubject(), templateString);
        return responseUtil.generateSuccessResponse("Email Sent Successfully");
    }
}
