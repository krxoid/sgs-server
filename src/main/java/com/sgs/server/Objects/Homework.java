package com.sgs.server.Objects;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@Table(name = "homeworks")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    public Standard standard;

    @Column(nullable = false)
    public short section;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    public Map<String, Object> work;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    public LocalDateTime createdAt;
}