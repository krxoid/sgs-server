package com.sgs.server;

import com.sgs.server.Objects.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServicer {

    @Value("${jwt.key}")
    private String secret;

    private SecretKey key;

    private static final long EXPIRATION_TIME =
            1000L * 60 * 60; // 1 hour

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(long userId, UserType userType) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userType", userType.name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long extractUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public UserType extractUserType(String token) {
        return UserType.valueOf(
                extractClaims(token).get("userType", String.class)
        );
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);

            // Require the subject and userType claim.
            if (claims.getSubject() == null
                    || claims.get("userType", String.class) == null) {
                return false;
            }

            Long.parseLong(claims.getSubject());
            UserType.valueOf(
                    claims.get("userType", String.class)
            );

            return true;
        } catch (JwtException | IllegalArgumentException
                 | NullPointerException ex) {
            return false;
        }
    }
}
