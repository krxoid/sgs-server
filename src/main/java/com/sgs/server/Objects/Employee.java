package com.sgs.server.Objects;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;
    public int salary;
    public LocalDate dob;
    public LocalDate hiringDate;

    public boolean isAdmin;

    public String hashedPassword;

    public boolean isEmployed;
}
