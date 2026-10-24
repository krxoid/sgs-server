package com.sgs.server.Tests;

import com.sgs.server.AuthenticationServicer;
import com.sgs.server.Servicer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationTester {

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
    void createStudentIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/auth/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void createEmployeeIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/auth/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void loginIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    private void assertEndpointRateLimited(
            MockHttpServletRequestBuilder endpoint
    ) throws Exception {

        for (int i = 0; i < 100; i++) {
            mockMvc.perform(
                    get("/api/status").with(request -> {
                        request.setRemoteAddr(ip);
                        return request;
                    })
            ).andExpect(status().isOk());
        }

        int actualStatus = mockMvc.perform(
                endpoint.with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
        ).andReturn().getResponse().getStatus();

        assertEquals(429, actualStatus,
                "Expected the endpoint to reject request 101");
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