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
class ControllerTester {

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
    void statusIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/status"));
    }

    @Test
    void getStudentIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/student/1"));
    }

    @Test
    void currentStudentIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/student/me"));
    }

    @Test
    void updateStudentIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                patch("/api/student/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void updateEmployeeIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                patch("/api/employee/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void createAnnouncementIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/api/announcement/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void updateAnnouncementIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/api/announcement/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void deleteAnnouncementIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/api/announcement/delete/1")
        );
    }

    @Test
    void getAnnouncementIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/announcement/get/1"));
    }

    @Test
    void visibleAnnouncementsIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/announcements/visible"));
    }

    @Test
    void getAnnouncementsIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/announcements/get"));
    }

    @Test
    void createHomeworkIsRateLimited() throws Exception {
        assertEndpointRateLimited(
                post("/api/homework/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        );
    }

    @Test
    void deleteHomeworkIsRateLimited() throws Exception {
        assertEndpointRateLimited(post("/api/homework/delete/1"));
    }

    @Test
    void getHomeworkIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/homework/get/1"));
    }

    @Test
    void getStudentsIsRateLimited() throws Exception {
        assertEndpointRateLimited(get("/api/students/get"));
    }

    private void assertEndpointRateLimited(
            MockHttpServletRequestBuilder endpoint
    ) throws Exception {

        // Consume the 100 requests allowed for this IP.
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(
                    get("/api/status").with(request -> {
                        request.setRemoteAddr(ip);
                        return request;
                    })
            ).andExpect(status().isOk());
        }

        // Request 101 must be rejected by the rate limiter.
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