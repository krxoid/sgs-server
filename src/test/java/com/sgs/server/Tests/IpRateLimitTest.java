package com.sgs.server.Tests;

import com.sgs.server.AuthenticationServicer;
import com.sgs.server.Servicer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IpRateLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Servicer servicer;

    @MockitoBean
    private AuthenticationServicer authenticationServicer;

    private String ip;

    @BeforeEach
    void setUp() {
        ip = uniqueIp();
    }

    @Test
    void allowsFirst100RequestsAndRejects101st() throws Exception {
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(
                    get("/api/status").with(request -> {
                        request.setRemoteAddr(ip);
                        return request;
                    })
            ).andExpect(status().isOk());
        }

        mockMvc.perform(
                get("/api/status").with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
        ).andExpect(status().isTooManyRequests());
    }

    @Test
    void rateLimitedResponseContainsRetryAfterHeader() throws Exception {
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(
                    get("/api/status").with(request -> {
                        request.setRemoteAddr(ip);
                        return request;
                    })
            ).andExpect(status().isOk());
        }

        MvcResult result = mockMvc.perform(
                        get("/api/status").with(request -> {
                            request.setRemoteAddr(ip);
                            return request;
                        })
                ).andExpect(status().isTooManyRequests())
                .andReturn();

        String retryAfter =
                result.getResponse().getHeader("Retry-After");

        assertNotNull(retryAfter, "Retry-After header should be present");
        assertTrue(
                Integer.parseInt(retryAfter) >= 1,
                "Retry-After should be a positive number of seconds"
        );
    }

    @Test
    void differentIpGetsItsOwnRateLimit() throws Exception {
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(
                    get("/api/status").with(request -> {
                        request.setRemoteAddr(ip);
                        return request;
                    })
            ).andExpect(status().isOk());
        }

        // The first IP is now limited.
        mockMvc.perform(
                get("/api/status").with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
        ).andExpect(status().isTooManyRequests());

        // A different IP should still have its own quota.
        mockMvc.perform(
                get("/api/status").with(request -> {
                    request.setRemoteAddr(uniqueIp());
                    return request;
                })
        ).andExpect(status().isOk());
    }

    private String uniqueIp() {
        String hex = UUID.randomUUID()
                .toString()
                .replace("-", "");

        return "2001:db8:"
                + hex.substring(0, 4) + ":"
                + hex.substring(4, 8) + ":"
                + hex.substring(8, 12) + ":"
                + hex.substring(12, 16) + ":"
                + hex.substring(16, 20) + ":"
                + hex.substring(20, 24);
    }
}