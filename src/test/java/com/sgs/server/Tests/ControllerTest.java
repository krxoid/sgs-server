package com.sgs.server.Tests;

import com.sgs.server.Controller;
import com.sgs.server.Servicer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Servicer servicer;

    @Test
    void searchStudentsByName() throws Exception {
        when(servicer.searchStudentByName("Rahul"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/students/search")
                        .param("name", "Rahul"))
                .andExpect(status().isOk());

        verify(servicer).searchStudentByName("Rahul");
    }

    @Test
    void searchStudentsWithPartialName() throws Exception {
        when(servicer.searchStudentByName("Rah"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/students/search")
                        .param("name", "Rah"))
                .andExpect(status().isOk());

        verify(servicer).searchStudentByName("Rah");
    }

    @Test
    void searchStudentsWithSpaces() throws Exception {
        when(servicer.searchStudentByName("Rahul Kumar"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/students/search")
                        .param("name", "Rahul Kumar"))
                .andExpect(status().isOk());

        verify(servicer).searchStudentByName("Rahul Kumar");
    }

    @Test
    void searchStudentsWithNoResults() throws Exception {
        when(servicer.searchStudentByName("Nobody"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/students/search")
                        .param("name", "Nobody"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(servicer).searchStudentByName("Nobody");
    }

    @Test
    void searchStudentsWithoutName() throws Exception {
        mockMvc.perform(get("/api/students/search"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(servicer);
    }

    @Test
    void searchStudentsWithDifferentCapitalization() throws Exception {
        when(servicer.searchStudentByName("RAHUL"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/students/search")
                        .param("name", "RAHUL"))
                .andExpect(status().isOk());

        verify(servicer).searchStudentByName("RAHUL");
    }
}
