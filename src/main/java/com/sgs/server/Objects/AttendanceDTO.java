package com.sgs.server.Objects;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttendanceDTO {
    private String standard;
    private LocalDateTime createdAt;
    private List<AttendanceRecordDTO> records;
}