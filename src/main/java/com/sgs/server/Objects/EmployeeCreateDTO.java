package com.sgs.server.Objects;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateDTO extends CreateDTO {
    private int salary;
    private LocalDate dob;
    private boolean isAdmin;
    private String password;

    private String token;
}