package com.bs.application.controllers;

import com.bs.application.dtos.MailRequest;
import com.bs.application.dtos.Response;
import com.bs.application.services.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
@PreAuthorize("hasRole('ADMIN')")
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('admin:write')")
    ResponseEntity<Response> sendMail(@RequestBody MailRequest mailRequest) throws MessagingException {
        return ResponseEntity.ok(mailService.sendEmail(mailRequest));
    }
}
