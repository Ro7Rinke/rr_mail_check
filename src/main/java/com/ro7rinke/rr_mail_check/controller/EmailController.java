package com.ro7rinke.rr_mail_check.controller;

import com.ro7rinke.rr_mail_check.model.Email;
import com.ro7rinke.rr_mail_check.repository.EmailRepository;
import com.ro7rinke.rr_mail_check.service.EmailValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailValidationService validationService;
    private final EmailRepository emailRepository;

    public EmailController(EmailValidationService validationService, EmailRepository emailRepository) {
        this.validationService = validationService;
        this.emailRepository = emailRepository;
    }

    @PostMapping("/validate")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Email> validateEmails(@RequestBody List<String> emails) {
        List<Email> results = new ArrayList<>();
        for (String address : emails) {
            String status = validationService.validateEmail(address);
            Email email = new Email();
            email.setEmail(address);
            email.setStatus(status);
            results.add(emailRepository.save(email));
        }
        return results;
    }
}
