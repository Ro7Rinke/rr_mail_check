package com.ro7rinke.rr_mail_check.controller;

import com.ro7rinke.rr_mail_check.service.DisposableDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/disposable-domains")
public class DisposableDomainController {

    private final DisposableDomainService domainService;

    public DisposableDomainController(DisposableDomainService domainService) {
        this.domainService = domainService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            int inserted = domainService.insertFromCSV(file);
            return ResponseEntity.ok("Inserted " + inserted + " domains successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}