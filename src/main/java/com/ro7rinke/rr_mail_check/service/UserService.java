// com.ro7rinke.rr_mail_check.service.UserService
package com.ro7rinke.rr_mail_check.service;

import com.ro7rinke.rr_mail_check.model.User;
import com.ro7rinke.rr_mail_check.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword, String role) {
        if (repo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role == null ? "ROLE_USER" : role);
        return repo.save(user);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }
}