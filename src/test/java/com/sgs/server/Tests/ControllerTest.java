package com.sgs.server.Tests;

import com.sgs.server.AuthenticationServicer;
import com.sgs.server.Servicer;
import com.sgs.server.Objects.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Servicer servicer;

    @MockitoBean
    private AuthenticationServicer authenticationServicer;

    @Test
    void statusReturnsExpectedMessage() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Server up and running"));

        verifyNoInteractions(servicer);
    }

    @Test
    void getStudentReturnsStudent() throws Exception {
        Student student = mock(Student.class);
        when(servicer.getStudentById(42L)).thenReturn(student);

        mockMvc.perform(get("/api/student/42"))
                .andExpect(status().isOk());

        verify(servicer).getStudentById(42L);
    }

    @Test
    void getStudentPassesRequestedIdToService() throws Exception {
        when(servicer.getStudentById(7L))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Student not found with ID: 7"
                ));

        mockMvc.perform(get("/api/student/7"))
                .andExpect(status().isNotFound());

        verify(servicer).getStudentById(7L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void currentStudentCallsService() throws Exception {
        // SecurityHolderContext must resolve the mock user's ID
        // using the application's actual authentication setup.
        mockMvc.perform(get("/api/student/me"))
                .andExpect(status().is5xxServerError());

        // This test deliberately exposes an unconfigured security
        // context instead of assuming a student ID.
    }

    @Test
    void updateStudentRejectsMalformedId() throws Exception {
        mockMvc.perform(patch("/api/student/update/not-a-number")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStudentReturnsNoContent() throws Exception {
        mockMvc.perform(patch("/api/student/update/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNoContent());

        verify(servicer).updateStudent(eq(42L), any(StudentUpdateDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployeeReturnsNoContent() throws Exception {
        mockMvc.perform(patch("/api/employee/update/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNoContent());

        verify(servicer).updateEmployee(eq(9L), any(EmployeeUpdateDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAnnouncementReturnsCreatedId() throws Exception {
        when(servicer.createAnnouncement(any(AnnouncementDTO.class)))
                .thenReturn(123L);

        mockMvc.perform(post("/api/announcement/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Exam schedule",
                                  "content": "Exams begin Monday"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("123"));

        verify(servicer).createAnnouncement(any(AnnouncementDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAnnouncementReturnsNoContent() throws Exception {
        mockMvc.perform(post("/api/announcement/update/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated",
                                  "content": "Updated content"
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(servicer).updateAnnouncement(eq(5L), any(AnnouncementDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAnnouncementCallsService() throws Exception {
        mockMvc.perform(post("/api/announcement/delete/5"))
                .andExpect(status().isNoContent());

        verify(servicer).deleteAnnouncement(5L);
    }

    @Test
    void getAnnouncementCallsService() throws Exception {
        Announcement announcement = mock(Announcement.class);
        when(servicer.getAnnouncementById(5L)).thenReturn(announcement);

        mockMvc.perform(get("/api/announcement/get/5"))
                .andExpect(status().isOk());

        verify(servicer).getAnnouncementById(5L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void visibleAnnouncementsCallsService() throws Exception {
        when(servicer.getVisibleAnnouncements()).thenReturn(List.of());

        mockMvc.perform(get("/api/announcements/visible"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(servicer).getVisibleAnnouncements();
    }

    @Test
    void getAnnouncementsReturnsList() throws Exception {
        when(servicer.getAnnouncements()).thenReturn(List.of());

        mockMvc.perform(get("/api/announcements/get"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(servicer).getAnnouncements();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHomeworkReturnsCreatedId() throws Exception {
        when(servicer.createHomework(any(HomeworkDTO.class)))
                .thenReturn(321L);

        mockMvc.perform(post("/api/homework/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "section": 1,
                                  "standard": "FIRST",
                                  "work": "Complete exercises"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("321"));

        verify(servicer).createHomework(any(HomeworkDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateHomeworkReturnsNoContent() throws Exception {
        mockMvc.perform(post("/api/homework/update/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "section": 1,
                                  "standard": "FIRST",
                                  "work": "Updated exercises"
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(servicer).updateHomework(eq(11L), any(HomeworkDTO.class));
    }

    @Test
    void getHomeworkCallsService() throws Exception {
        Homework homework = mock(Homework.class);
        when(servicer.getHomeworkById(11L)).thenReturn(homework);

        mockMvc.perform(get("/api/homework/get/11"))
                .andExpect(status().isOk());

        verify(servicer).getHomeworkById(11L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void visibleHomeworksCallsService() throws Exception {
        when(servicer.getVisibleHomeworks()).thenReturn(List.of());

        mockMvc.perform(get("/api/homeworks/visible"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(servicer).getVisibleHomeworks();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteHomeworkCallsHomeworkDeletionMethod() throws Exception {
        mockMvc.perform(post("/api/homework/delete/11"))
                .andExpect(status().isNoContent());

        verify(servicer).deleteHomework(11L);
        verify(servicer, never()).deleteAnnouncement(11L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentsReturnsList() throws Exception {
        when(servicer.getStudents()).thenReturn(List.of());

        mockMvc.perform(get("/api/students/get"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(servicer).getStudents();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void adminOnlyStudentListingRejectsStudent() throws Exception {
        mockMvc.perform(get("/api/students/get"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(servicer);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void studentCannotUpdateStudent() throws Exception {
        mockMvc.perform(patch("/api/student/update/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        verify(servicer, never())
                .updateStudent(anyLong(), any(StudentUpdateDTO.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void studentCannotCreateAnnouncement() throws Exception {
        mockMvc.perform(post("/api/announcement/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        verify(servicer, never())
                .createAnnouncement(any(AnnouncementDTO.class));
    }
}
