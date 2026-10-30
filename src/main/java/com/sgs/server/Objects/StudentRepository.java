package com.sgs.server.Objects;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByScholarNumber(Long scholarNumber);
    List<Student> findByNameContainingIgnoreCase(String name);
}
