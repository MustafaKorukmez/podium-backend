package com.korukmez.podium.platform.competition.service;

import com.korukmez.podium.platform.competition.dto.CompetitionCreateDTO;
import com.korukmez.podium.platform.competition.dto.CompetitionResponseDTO;
import com.korukmez.podium.platform.competition.entity.Competition;
import com.korukmez.podium.platform.competition.entity.Round;
import com.korukmez.podium.platform.competition.repository.CompetitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    // (İhtiyaç duyacağımız RoundRepository'yi henüz eklemedik,
    // bir sonraki adımda ekleyeceğiz)

    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    /**
     * Yeni bir yarışmayı ve ona bağlı tüm turları (rounds) oluşturur.
     */
    @Transactional // Bu metot çok önemlidir.
    // Ya BÜTÜN turlar ve yarışma oluşur, ya da hata olursa HİÇBİRİ oluşmaz.
    public CompetitionResponseDTO createCompetition(CompetitionCreateDTO dto) {

        // 1. Ana Yarışma (Competition) nesnesini oluştur
        Competition competition = new Competition();
        competition.setName(dto.getName());
        competition.setDescription(dto.getDescription());
        // 'status' ve 'createdAt' alanları entity'de varsayılan değerlere sahip

        // 2. Turları (Rounds) DTO'dan Entity'ye dönüştür
        dto.getRounds().forEach(roundDTO -> {
            Round round = new Round();
            round.setName(roundDTO.getName());
            round.setRoundOrder(roundDTO.getRoundOrder());
            round.setGroupSize(roundDTO.getGroupSize());
            round.setParticipantsToAdvance(roundDTO.getParticipantsToAdvance());
            round.setScoringType(roundDTO.getScoringType());

            // 3. İŞ KURALI: Turu, ana yarışmaya bağla
            // Bu, 'competition.rounds' listesine eklemekten daha önemlidir
            round.setCompetition(competition);

            // 4. İLİŞKİ YÖNETİMİ: Turu, ana yarışmanın listesine de ekle
            // Bu, JPA'nın ilişkiyi doğru yönetmesi için (CascadeType.ALL) gereklidir.
            competition.getRounds().add(round);
        });

        // 5. Kaydet
        Competition savedCompetition = competitionRepository.save(competition);
        // 6. Kaydedilmiş Entity'yi GÜVENLİ DTO'ya çevir ve onu döndür
        return CompetitionResponseDTO.fromEntity(savedCompetition);
    }

    // ... (getCompetitionDetails vb. metotlar buraya eklenecek) ...
}