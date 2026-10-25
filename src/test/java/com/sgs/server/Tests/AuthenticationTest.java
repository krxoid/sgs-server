package com.sgs.server.Tests;

import com.sgs.server.AuthenticationServicer;
import com.sgs.server.Objects.EmployeeCreateDTO;
import com.sgs.server.Objects.LoginDTO;
import com.sgs.server.Objects.StudentCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationServicer authenticationServicer;

    @Test
    void createStudentReturnsScholarNumber() throws Exception {
        when(authenticationServicer.create(any(StudentCreateDTO.class)))
                .thenReturn(12345L);

        mockMvc.perform(post("/auth/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("12345"));

        verify(authenticationServicer)
                .create(any(StudentCreateDTO.class));
    }

    @Test
    void createEmployeeReturnsIdNumber() throws Exception {
        when(authenticationServicer.create(any(EmployeeCreateDTO.class)))
                .thenReturn(42L);

        mockMvc.perform(post("/auth/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));

        verify(authenticationServicer)
                .create(any(EmployeeCreateDTO.class));
    }

    @Test
    void loginReturnsServiceResult() throws Exception {
        when(authenticationServicer.login(any(LoginDTO.class)))
                .thenReturn("test-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("test-token"));

        verify(authenticationServicer)
                .login(any(LoginDTO.class));
    }

    @Test
    void createStudentRejectsMalformedJson() throws Exception {
        mockMvc.perform(post("/auth/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationServicer);
    }

    @Test
    void createEmployeeRejectsMalformedJson() throws Exception {
        mockMvc.perform(post("/auth/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationServicer);
    }

    @Test
    void loginRejectsMalformedJson() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationServicer);
    }
}
