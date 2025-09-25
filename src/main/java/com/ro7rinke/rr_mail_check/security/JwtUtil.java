// com.ro7rinke.rr_mail_check.security.JwtUtil
package com.ro7rinke.rr_mail_check.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil() {
        String secret = System.getProperty("JWT_SECRET");
        byte[] keyBytes = secret.getBytes();
        this.key = Keys.hmacShaKeyFor(java.util.Arrays.copyOf(keyBytes, Math.max(32, keyBytes.length)));
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        // 24h
        long expirationMs = 1000L * 60 * 60 * 24;
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