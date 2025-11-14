package com.korukmez.podium.platform.scoring.repository;

import com.korukmez.podium.platform.scoring.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    // Burası 'calculate averages' gibi işlemler için kritik olacak
}