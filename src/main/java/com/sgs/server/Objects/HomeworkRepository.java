package com.sgs.server.Objects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    @Query(value = """
    SELECT *
    FROM announcements
    WHERE standard = :standard
    AND
    WHERE section = :section
    """, nativeQuery = true)
    List<Homework> findVisibleHomeworks(
            @Param("standard") String standard,
            @Param("section") short section
    );
}