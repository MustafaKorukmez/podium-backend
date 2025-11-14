package com.korukmez.podium.platform.scoring.repository;

import com.korukmez.podium.platform.scoring.entity.JudgeRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeRankingRepository extends JpaRepository<JudgeRanking, Long> {
    // Burası 'calculate averages' gibi işlemler için kritik olacak
}