package com.korukmez.podium.platform.competition.repository;

import com.korukmez.podium.platform.competition.entity.CompetitionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRegistrastionRepository extends JpaRepository<CompetitionRegistration, Long> {
}
