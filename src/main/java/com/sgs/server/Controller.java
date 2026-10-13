package com.sgs.server;

import com.sgs.server.Objects.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    private final Servicer servicer;
    private final EmployeeRepository employeeRepository;

    public Controller(
            Servicer servicer,
            EmployeeRepository employeeRepository
    ) {
        this.servicer = servicer;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Server up and running");
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        Optional<Student> student = servicer.getStudentById(id);

        if (student.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid Scholar Number");
        }

        return ResponseEntity.ok(student.get());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/student/update/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateDTO dto
    ) {
        Optional<Student> student = servicer.getStudentById(id);

        if (student.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid Scholar Number");
        }

        servicer.updateStudent(student.get(), dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/employee/update/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateDTO dto
    ) {
        Optional<Employee> employee = servicer.getEmployeeById(id);

        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        servicer.updateEmployee(employee.get(), dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/create")
    public ResponseEntity<?> createAnnouncement(
            @RequestBody AnnouncementDTO dto
    ) {
        Long userId = SecurityHolderContext.getCurrentUserId();
        UserType userType = SecurityHolderContext.getCurrentUserType();

        if (userId == null || userType != UserType.EMPLOYEE) {
            return forbidden("Only administrators can create announcements");
        }

        servicer.createAnnouncement(dto, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/announcements/get/{id}")
    public ResponseEntity<?> getAnnouncement(@PathVariable Long id) {

        Optional<Announcement> announcement = servicer.getAnnouncementById(id);

        if (announcement.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid Announcement Id");
        }

        return ResponseEntity.ok()
                .body(announcement.get());
    }

    @GetMapping("/announcements/get")
    public ResponseEntity<?> getAnnouncements() {
        return ResponseEntity.ok(servicer.getAnnouncements());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/get")
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
