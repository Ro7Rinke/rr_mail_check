package com.ro7rinke.rr_mail_check.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private Key key;

    @Value("${jwt.secret:default_super_secret_key_please_change}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // default 24h
    private long expirationMs;

    @PostConstruct
    public void init() {
        byte[] keyBytes = java.util.Arrays.copyOf(secret.getBytes(), Math.max(32, secret.getBytes().length));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String getUsername(String token) {
        return validateToken(token).getBody().getSubject();
    }

    public String getRole(String token) {
        Object r = validateToken(token).getBody().get("role");
        return r == null ? null : r.toString();
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}