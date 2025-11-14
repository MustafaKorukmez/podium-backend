package com.korukmez.podium.platform.competition.repository;

import com.korukmez.podium.platform.competition.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    // İleride buraya 'findByCompetitionId' gibi sorgular ekleyebiliriz
}