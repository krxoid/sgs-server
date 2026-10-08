package com.sgs.server.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgs.server.Objects.Standard;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "students")
public class Student {

    public String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long scholarNumber;

    public LocalDate dob;
    public LocalDate admissionDate;
    public int rollNumber;

    @Enumerated(EnumType.STRING)
    public Standard standard;

    @Enumerated(EnumType.STRING)
    public Title title;

    public byte section;
    // 0 = Kalam | 1 = Bose | 2 = Raman

    public boolean isSuspended;

    @JsonIgnore //Stops API from returning hashed password too not necessary but good measure
    public String hashedPassword;

    public boolean isEnrolled;

}
