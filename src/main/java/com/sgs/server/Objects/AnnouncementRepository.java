package com.sgs.server.Objects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Query(value = """
    SELECT *
    FROM announcements
    WHERE (
        cardinality(classes) = 0
        OR :standard = ANY(classes)
    )
    AND (
        cardinality(sections) = 0
        OR :section = ANY(sections)
    )
    """, nativeQuery = true)
    List<Announcement> findVisibleAnnouncements(
            @Param("standard") String standard,
            @Param("section") short section
    );
}
