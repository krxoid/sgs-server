package com.sgs.server.Objects;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "attendance_records")
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long attendanceId;
    public Long scholarNumber;
    public Boolean present;
}