package com.sgs.server;

import com.sgs.server.Objects.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(servicer.getStudentById(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/me")
    public ResponseEntity<?> currentStudent() {
        return ResponseEntity.ok(
                servicer.getStudentById(
                        SecurityHolderContext.getCurrentUserId()
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/count")
    public ResponseEntity<Long> getStudentCount() {
        return ResponseEntity.ok(servicer.getStudentCount());
    }

    @GetMapping("/students/search")
    public ResponseEntity<?> searchStudentByName(@RequestParam String name) {
        return ResponseEntity.ok(servicer.searchStudentByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/latest")
    public ResponseEntity<?> getLatestStudents(@RequestParam(defaultValue = "10") Long count) {
        return ResponseEntity.ok(servicer.getLatestStudents(count));
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
    @GetMapping("/students/get")
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(servicer.getStudents());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/employee/get/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(servicer.getEmployeeById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/employee/me")
    public ResponseEntity<?> currentEmployee() {
        return ResponseEntity.ok(
                servicer.getEmployeeById(
                        SecurityHolderContext.getCurrentUserId()
                )
        );
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

    @GetMapping("/announcement/get/{id}")
    public ResponseEntity<?> getAnnouncement(@PathVariable Long id) {
        return ResponseEntity.ok(servicer.getAnnouncementById(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/announcements/visible")
    public ResponseEntity<?> getVisibleAnnouncements() {
        return ResponseEntity.ok(servicer.getVisibleAnnouncements());
    }

    @GetMapping("/announcements/search")
    public ResponseEntity<?> searchAnnouncementByName(@RequestParam String name) {
        return ResponseEntity.ok(servicer.searchAnnouncementByName(name));
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
    @PostMapping("/homework/delete/{id}")
    public ResponseEntity<?> deleteHomework(@PathVariable Long id) {
        servicer.deleteHomework(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/homework/update/{id}")
    public ResponseEntity<?> updateHomework(
            @PathVariable Long id,
            @RequestBody HomeworkDTO dto
    ) {
        servicer.updateHomework(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/students/suspended")
    public ResponseEntity<?> getSuspendedStudents() {
        return ResponseEntity.ok(
                servicer.getSuspendedStudents()
        );
    }

    @GetMapping("/homework/get/{id}")
    public ResponseEntity<?> getHomework(@PathVariable Long id) {
        return ResponseEntity.ok().body(servicer.getHomeworkById(id));
    }

    @GetMapping("/homeworks/visible")
    public ResponseEntity<?> getVisibleHomeworks() {
        return ResponseEntity.ok(servicer.getVisibleAnnouncements());
    }

}
