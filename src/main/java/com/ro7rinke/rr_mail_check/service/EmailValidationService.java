package com.ro7rinke.rr_mail_check.service;

import jakarta.annotation.PostConstruct;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.naming.directory.*;
import javax.naming.NamingException;
import java.util.List;

@Service
public class EmailValidationService {

    private final JdbcTemplate jdbcTemplate;
    private EmailValidator emailValidator;
    private List<String> disposableDomains;
    private List<String> roleAccounts;

    public EmailValidationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        emailValidator = EmailValidator.getInstance();

        roleAccounts = List.of("admin","support","info","sales","contact");

        disposableDomains = jdbcTemplate.queryForList(
                "SELECT domain FROM disposable_domains", String.class
        );
    }

    public String validateEmail(String email) {
        if (!emailValidator.isValid(email)) {
            return "INVALID_FORMAT";
        }

        String[] parts = email.split("@");
        String userPart = parts[0];
        String domain = parts[1];

        if (disposableDomains.contains(domain.toLowerCase())) {
            return "DISPOSABLE";
        }

        if (roleAccounts.contains(userPart.toLowerCase())) {
            return "ROLE_BASED";
        }

        if (!hasMXRecord(domain)) {
            return "INVALID_DOMAIN";
        }

        return "VALID";
    }

    private boolean hasMXRecord(String domain) {
        try {
            java.util.Hashtable<String, String> env = new java.util.Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            return attrs.get("MX") != null;
        } catch (NamingException e) {
            return false;
        }
    }
}