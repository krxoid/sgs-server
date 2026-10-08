package com.sgs.server;

import com.sgs.server.Objects.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller{

    private Servicer servicer;
    private JWTServicer jwtServicer;

    Controller(
            Servicer servicer,
            JWTServicer jwtServicer
    ) {
        this.servicer = servicer;
        this.jwtServicer = jwtServicer;
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(){
        return ResponseEntity.ok().body("Server up and running");
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {

        Optional<Student> student = servicer.getStudentById(id);

        if (student.isPresent())
            return ResponseEntity
                    .ok()
                    .body(
                            student.get()
                    );

        return ResponseEntity.badRequest().body("Invalid Scholar Number");
    }

    @PatchMapping("/student/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody StudentUpdateDTO dto) {

        Optional<Student> student = servicer.getStudentById(id);

        if (student.isPresent()) {
            servicer.updateStudent(student.get(), dto);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().body("Invalid Scholar Number");
    }

    @PatchMapping("/employee/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EmployeeUpdateDTO dto) {

        Optional<Employee> employee = servicer.getEmployeeById(id);

        if (employee.isPresent()) {
            servicer.updateEmployee(employee.get(), dto);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getusers")
    public ResponseEntity<?> getStudents(@RequestParam String token) {

        if (servicer.getEmployeeById(
                jwtServicer.extractUserId(token, UserType.EMPLOYEE)
        ).map(Employee::isAdmin).orElse(false)) {
            return ResponseEntity.ok()
                    .body(servicer.getStudents());
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "FORBIDDEN",
                        "message", "You are not authorized to perform this action"
                ));
    }
}