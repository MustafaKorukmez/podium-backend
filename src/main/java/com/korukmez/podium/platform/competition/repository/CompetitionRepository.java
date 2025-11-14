package com.korukmez.podium.platform.competition.repository;

import com.korukmez.podium.platform.competition.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    // İleride buraya 'findByName' gibi özel sorgular ekleyebiliriz
}