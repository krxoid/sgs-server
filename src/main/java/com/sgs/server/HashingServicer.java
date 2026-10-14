package com.sgs.server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashingServicer{

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }
}