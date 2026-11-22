package com.sgs.server;

import com.sgs.server.Objects.*;
import com.sgs.server.Servicer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "SGS API",
        description = "Endpoints for managing students, employees, announcements, homework and attendance"
)
@RestController
@RequestMapping("/api/v1")
public class Controller {

    private final Servicer servicer;

    public Controller(Servicer servicer) {
        this.servicer = servicer;
    }

    // General

    @Operation(summary = "Check server status",
            description = "Returns a status message to verify that the API is running.")
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Server up and running");
    }

    // Students

    @Operation(summary = "Get student by ID",
            description = "Retrieves a student using their unique ID.")
    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(servicer.getStudentById(id));
    }

    @Operation(summary = "Get current student",
            description = "Retrieves the currently authenticated student's profile. Requires STUDENT role.")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/me")
    public ResponseEntity<?> currentStudent() {
        return ResponseEntity.ok(
                servicer.getStudentById(
                        SecurityHolderContext.getCurrentUserId()
                )
        );
    }

    @Operation(summary = "Get student count",
            description = "Returns the total number of students. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/count")
    public ResponseEntity<Long> getStudentCount() {
        return ResponseEntity.ok(servicer.getStudentCount());
    }

    @Operation(summary = "Search students",
            description = "Searches for students by name.")
    @GetMapping("/students/search")
    public ResponseEntity<?> searchStudentByName(@RequestParam String name) {
        return ResponseEntity.ok(servicer.searchStudentByName(name));
    }

    @Operation(summary = "Get latest students",
            description = "Returns the most recently added students. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/latest")
    public ResponseEntity<?> getLatestStudents(
            @RequestParam(defaultValue = "10") Long count
    ) {
        return ResponseEntity.ok(servicer.getLatestStudents(count));
    }

    @Operation(summary = "Update student",
            description = "Partially updates a student's information. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/student/update/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateDTO dto
    ) {
        servicer.updateStudent(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all students",
            description = "Returns the complete student collection. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/get")
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(servicer.getStudents());
    }

    @Operation(summary = "Get suspended students",
            description = "Returns suspended students. Requires ADMIN or EMPLOYEE role.")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/students/suspended")
    public ResponseEntity<?> getSuspendedStudents() {
        return ResponseEntity.ok(servicer.getSuspendedStudents());
    }

    // Employees

    @Operation(summary = "Get employee by ID",
            description = "Retrieves an employee by their unique ID. Requires ADMIN or EMPLOYEE role.")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/employee/get/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(servicer.getEmployeeById(id));
    }

    @Operation(summary = "Get current employee",
            description = "Retrieves the currently authenticated employee's profile. Requires ADMIN or EMPLOYEE role.")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/employee/me")
    public ResponseEntity<?> currentEmployee() {
        return ResponseEntity.ok(
                servicer.getEmployeeById(
                        SecurityHolderContext.getCurrentUserId()
                )
        );
    }

    @Operation(summary = "Update employee",
            description = "Partially updates employee information. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/employee/update/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateDTO dto
    ) {
        servicer.updateEmployee(id, dto);
        return ResponseEntity.noContent().build();
    }

    // Announcements

    @Operation(summary = "Create announcement",
            description = "Creates a new announcement. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/create")
    public ResponseEntity<Long> createAnnouncement(
            @RequestBody AnnouncementDTO dto
    ) {
        return ResponseEntity.ok(servicer.createAnnouncement(dto));
    }

    @Operation(summary = "Update announcement",
            description = "Partially updates an announcement. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/announcement/update/{id}")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementDTO dto
    ) {
        servicer.updateAnnouncement(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete announcement",
            description = "Deletes an announcement by ID. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        servicer.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get announcement",
            description = "Retrieves an announcement by ID.")
    @GetMapping("/announcement/get/{id}")
    public ResponseEntity<?> getAnnouncement(@PathVariable Long id) {
        return ResponseEntity.ok(servicer.getAnnouncementById(id));
    }

    @Operation(summary = "Get visible announcements",
            description = "Retrieves announcements visible to students. Requires STUDENT role.")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/announcements/visible")
    public ResponseEntity<?> getVisibleAnnouncements() {
        return ResponseEntity.ok(servicer.getVisibleAnnouncements());
    }

    @Operation(summary = "Search announcements",
            description = "Searches announcements by name.")
    @GetMapping("/announcements/search")
    public ResponseEntity<?> searchAnnouncementByName(@RequestParam String name) {
        return ResponseEntity.ok(servicer.searchAnnouncementByName(name));
    }

    @Operation(summary = "Get all announcements",
            description = "Returns the complete announcement collection.")
    @GetMapping("/announcements/get")
    public ResponseEntity<?> getAnnouncements() {
        return ResponseEntity.ok(servicer.getAnnouncements());
    }

    // Homework

    @Operation(summary = "Create homework",
            description = "Creates a homework entry. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/homework/create")
    public ResponseEntity<Long> createHomework(@RequestBody HomeworkDTO dto) {
        return ResponseEntity.ok(servicer.createHomework(dto));
    }

    @Operation(summary = "Delete homework",
            description = "Deletes homework by ID. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/homework/delete/{id}")
    public ResponseEntity<?> deleteHomework(@PathVariable Long id) {
        servicer.deleteHomework(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update homework",
            description = "Partially updates homework information. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/homework/update/{id}")
    public ResponseEntity<?> updateHomework(
            @PathVariable Long id,
            @RequestBody HomeworkDTO dto
    ) {
        servicer.updateHomework(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get homework",
            description = "Retrieves homework by ID.")
    @GetMapping("/homework/get/{id}")
    public ResponseEntity<?> getHomework(@PathVariable Long id) {
        return ResponseEntity.ok(servicer.getHomeworkById(id));
    }

    @Operation(summary = "Get visible homework",
            description = "Returns homework entries visible to users.")
    @GetMapping("/homeworks/visible")
    public ResponseEntity<?> getVisibleHomeworks() {
        return ResponseEntity.ok(servicer.getVisibleHomeworks());
    }

    // Attendance

    @Operation(summary = "Log attendance",
            description = "Records attendance information. Requires EMPLOYEE role.")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/attendance/log")
    public ResponseEntity<?> logAttendance(@RequestBody AttendanceDTO dto) {
        return ResponseEntity.ok(servicer.logAttendance(dto));
    }

    @Operation(summary = "Update attendance",
            description = "Updates an existing attendance record. Requires ADMIN or EMPLOYEE role.")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PatchMapping("/attendance/update/{id}")
    public ResponseEntity<?> updateAttendance(
            @PathVariable Long id,
            @RequestBody AttendanceDTO dto
    ) {
        servicer.updateAttendance(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete attendance",
            description = "Deletes an attendance record by ID. Requires ADMIN or EMPLOYEE role.")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PostMapping("/attendance/delete/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id) {
        servicer.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
