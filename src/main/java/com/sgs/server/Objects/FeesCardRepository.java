package com.sgs.server.Objects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeesCardRepository extends JpaRepository<FeesCard, UUID> {
}