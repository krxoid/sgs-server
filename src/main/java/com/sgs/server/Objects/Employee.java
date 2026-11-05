package com.sgs.server.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public String hashedPassword;

    public boolean isEmployed;
}
