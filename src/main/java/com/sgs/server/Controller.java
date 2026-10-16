package com.sgs.server;

import com.sgs.server.Objects.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.AnnotatedConstruct;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    private final Servicer servicer;

    public Controller(
            Servicer servicer
    ) {
        this.servicer = servicer;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Server up and running");
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        Optional<Student> student = servicer.getStudentById(id);

        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student.get());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/student/update/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateDTO dto
    ) {
        servicer.updateStudent(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/employee/update/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateDTO dto
    ) {
        servicer.updateEmployee(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/create")
    public ResponseEntity<?> createAnnouncement(
            @RequestBody AnnouncementDTO dto
    ) {
        servicer.createAnnouncement(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/update/{id}")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementDTO dto
    ) {

        servicer.updateAnnouncement(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        servicer.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/announcements/get/{id}")
    public ResponseEntity<?> getAnnouncement(@PathVariable Long id) {

        Optional<Announcement> announcement = servicer.getAnnouncementById(id);

        if (announcement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(announcement.get());
    }

    @GetMapping("/announcements/get")
    public ResponseEntity<?> getAnnouncements() {
        return ResponseEntity.ok(servicer.getAnnouncements());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/get")
    public ResponseEntity<?> getStudents() {
        Long userId = SecurityHolderContext.getCurrentUserId();
        UserType userType = SecurityHolderContext.getCurrentUserType();

        if (userId == null
                || userType != UserType.EMPLOYEE) {
            return forbidden(
                    "You are not authorized to perform this action"
            );
        }

        return ResponseEntity.ok(servicer.getStudents());
    }

    private ResponseEntity<Map<String, String>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "FORBIDDEN",
                        "message", message
                ));
    }
}
