package com.sgs.server;

import com.sgs.server.Objects.*;

import jakarta.persistence.GeneratedValue;
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
    public ResponseEntity<Long> createAnnouncement(
            @RequestBody AnnouncementDTO dto
    ) {
        return ResponseEntity.ok(servicer.createAnnouncement(dto));
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

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/announcements/visible")
    public ResponseEntity<?> getVisibleAnnouncements() {
        return ResponseEntity.ok(servicer.getVisibleAnnouncements());
    }

    @GetMapping("/announcements/get")
    public ResponseEntity<?> getAnnouncements() {
        return ResponseEntity.ok(servicer.getAnnouncements());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/homework/create")
    public ResponseEntity<Long> createHomework(@RequestBody HomeworkDTO dto) {
        return ResponseEntity.ok(servicer.createHomework(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/get")
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(servicer.getStudents());
    }
}
