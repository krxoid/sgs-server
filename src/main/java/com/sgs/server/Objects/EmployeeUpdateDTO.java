package com.sgs.server.Objects;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeUpdateDTO extends UpdateDTO {
    public String name;
    public Integer salary;
    public LocalDate dob;
    public Boolean isAdmin;
    public String password;
    public Boolean isEmployed;
}