package com.ro7rinke.rr_mail_check.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DisposableDomainService {

    private final JdbcTemplate jdbcTemplate;

    public DisposableDomainService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertFromCSV(MultipartFile file) throws Exception {
        Set<String> domains = getStrings(file);

        String sql = "INSERT INTO disposable_domains(domain) VALUES (?) ON CONFLICT(domain) DO NOTHING";
        List<Object[]> batchArgs = domains.stream()
                .map(d -> new Object[]{d})
                .toList();

        int[] result = jdbcTemplate.batchUpdate(sql, batchArgs);

        // soma o número de inserções efetivas
        return Arrays.stream(result).sum();
    }

    private static Set<String> getStrings(MultipartFile file) throws IOException {
        Set<String> domains = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                line = line.trim();
                if (!line.isEmpty()) {
                    domains.add(line.toLowerCase());
                }
            }
        }
        return domains;
    }
}