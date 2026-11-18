package com.sgs.server.Objects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByAttendanceId(Long attendanceId);
    Optional<AttendanceRecord> findByAttendanceIdAndScholarNumber(
            Long attendanceId,
            Long scholarNumber
    );
}