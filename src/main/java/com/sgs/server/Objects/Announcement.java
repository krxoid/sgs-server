package com.sgs.server.Objects;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public String content;
    public Long author; //employees.id_number
    public LocalDateTime createdAt;

    public Integer[] classes;
}
