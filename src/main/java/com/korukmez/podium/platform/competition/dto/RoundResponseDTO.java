package com.korukmez.podium.platform.competition.dto;

import com.korukmez.podium.platform.competition.entity.Round;
import com.korukmez.podium.platform.core.enums.ScoringType;
import com.korukmez.podium.platform.core.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundResponseDTO {

    private Long id;
    private String name;
    private int roundOrder;
    private int groupSize;
    private int participantsToAdvance;
    private ScoringType scoringType;
    private Status status;

    // ÖNEMLİ: Bu DTO'da 'Competition' alanı YOK.
    // Bu sayede sonsuz döngüyü kırıyoruz.

    public static RoundResponseDTO fromEntity(Round round) {
        RoundResponseDTO dto = new RoundResponseDTO();
        dto.setId(round.getId());
        dto.setName(round.getName());
        dto.setRoundOrder(round.getRoundOrder());
        dto.setGroupSize(round.getGroupSize());
        dto.setParticipantsToAdvance(round.getParticipantsToAdvance());
        dto.setScoringType(round.getScoringType());
        dto.setStatus(round.getStatus());
        return dto;
    }
}