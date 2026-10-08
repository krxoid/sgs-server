package com.sgs.server;

import com.sgs.server.Objects.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private JWTServicer jwtServicer;
    private AuthenticationServicer authenticationServicer;
    private EmployeeRepository employeeRepository;

    AuthenticationController(
            JWTServicer jwtServicer,
            AuthenticationServicer authenticationServicer,
            EmployeeRepository employeeRepository
    ) {
        this.jwtServicer = jwtServicer;
        this.authenticationServicer = authenticationServicer;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/student/create")
    public ResponseEntity<?> create(@RequestBody StudentCreateDTO dto) {

        if (isAdmin(dto.getToken())) {
            Long scholarNumber = authenticationServicer.create(dto);
            return ResponseEntity.ok().body(scholarNumber);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "FORBIDDEN",
                        "message", "You are not authorized to perform this action"
                ));
    }

    @PostMapping("/employee/create")
    public ResponseEntity<?> create(@RequestBody EmployeeCreateDTO dto) {

        if (isAdmin(dto.getToken())) {
            Long idNumber = authenticationServicer.create(dto);
            return ResponseEntity.ok().body(idNumber);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "FORBIDDEN",
                        "message", "You are not authorized to perform this action"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok().body(authenticationServicer.login(dto));
    }

    public boolean isAdmin(String token) {
        return employeeRepository
                .findByIdNumber(jwtServicer.extractUserId(token, UserType.EMPLOYEE))
                .map(Employee::isAdmin)
                .orElse(false);
    }

}
