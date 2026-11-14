package com.sgs.server.Objects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByAttendanceId(Long attendanceId);
}