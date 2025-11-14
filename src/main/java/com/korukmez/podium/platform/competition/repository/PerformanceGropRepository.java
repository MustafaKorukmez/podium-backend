package com.korukmez.podium.platform.competition.repository;

import com.korukmez.podium.platform.competition.entity.PerformanceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceGropRepository extends JpaRepository<PerformanceGroup, Long> {
}
