package com.korukmez.podium.platform.competition.repository;

import com.korukmez.podium.platform.competition.entity.CompetitionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRegistrationRepository extends JpaRepository<CompetitionRegistration, Long> {

    // İleride buraya "bir kullanıcının bir yarışmaya zaten kayıtlı olup olmadığını"
    // hızlıca kontrol etmek için bir metot ekleyeceğiz.
    // Örn: boolean existsByUserAndCompetition(User user, Competition competition);
}