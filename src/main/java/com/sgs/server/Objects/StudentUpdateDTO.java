package com.sgs.server.Objects;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentUpdateDTO extends UpdateDTO {
    private LocalDate dob;
    private Integer rollNumber;
    private String standard;
    private String title;
    private Byte section;
    private String password;

    private String token;
}