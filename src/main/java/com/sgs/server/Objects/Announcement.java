package com.sgs.server.Objects;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 200)
    public String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String content;

    @Column(nullable = false)
    public Long author;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    public LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    public Standard[] classes;

    // PostgreSQL SMALLINT[] — multiple sections can be targeted.
    // Requires appropriate Hibernate array mapping.
    public Byte[] sections;
}