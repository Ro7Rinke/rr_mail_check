// com.ro7rinke.rr_mail_check.controller.AuthController
package com.ro7rinke.rr_mail_check.controller;

import com.ro7rinke.rr_mail_check.dto.AuthRequest;
import com.ro7rinke.rr_mail_check.dto.AuthResponse;
import com.ro7rinke.rr_mail_check.model.User;
import com.ro7rinke.rr_mail_check.security.JwtUtil;
import com.ro7rinke.rr_mail_check.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        User created = userService.register(req.username(), req.password(), "ROLE_USER");
        return ResponseEntity.ok(created.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        // autentica via AuthenticationManager (verifica senha)
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        User user = userService.findByUsername(req.username());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}