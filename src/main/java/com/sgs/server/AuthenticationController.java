package com.sgs.server;

import com.sgs.server.Objects.EmployeeCreateDTO;
import com.sgs.server.Objects.LoginDTO;
import com.sgs.server.Objects.StudentCreateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationServicer authenticationServicer;

    public AuthenticationController(
            AuthenticationServicer authenticationServicer
    ) {
        this.authenticationServicer = authenticationServicer;
    }

    @PostMapping("/student/create")
    public ResponseEntity<Long> createStudent(
            @RequestBody StudentCreateDTO dto
    ) {
        Long scholarNumber = authenticationServicer.create(dto);
        return ResponseEntity.ok(scholarNumber);
    }

    @PostMapping("/employee/create")
    public ResponseEntity<Long> createEmployee(
            @RequestBody EmployeeCreateDTO dto
    ) {
        Long idNumber = authenticationServicer.create(dto);
        return ResponseEntity.ok(idNumber);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authenticationServicer.login(dto));
    }
}
