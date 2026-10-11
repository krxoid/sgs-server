package com.sgs.server.Objects;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentCreateDTO extends CreateDTO {
    private LocalDate dob;
    private int rollNumber;
    private String standard;
    private String title;
    private byte section;
    private String password;

    private String token;
}