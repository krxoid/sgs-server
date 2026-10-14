package com.sgs.server;

import com.sgs.server.Objects.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JWTServicer {

    @Value("${jwt.key}")
    private String secret;

    private Key key;

    // Token validity: 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(long userId, UserType userType) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userType", userType.name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public long extractUserId(String token, UserType userType) {
        return Long.parseLong(
                Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    public UserType extractUserType(String token) {
        String userType = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userType", String.class);

        return UserType.valueOf(userType);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}