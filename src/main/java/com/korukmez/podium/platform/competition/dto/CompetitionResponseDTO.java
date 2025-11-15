package com.korukmez.podium.platform.competition.dto;

import com.korukmez.podium.platform.competition.entity.Competition;
import com.korukmez.podium.platform.core.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CompetitionResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Status status;
    private List<RoundResponseDTO> rounds; // <-- Round'ların DTO listesi (Bu artık güvenli)

    public static CompetitionResponseDTO fromEntity(Competition competition) {
        CompetitionResponseDTO dto = new CompetitionResponseDTO();
        dto.setId(competition.getId());
        dto.setName(competition.getName());
        dto.setDescription(competition.getDescription());
        dto.setStatus(competition.getStatus());

        // İlişkili Round Entity'lerini Round DTO'larına dönüştür
        if (competition.getRounds() != null) {
            dto.setRounds(
                    competition.getRounds().stream()
                            .map(RoundResponseDTO::fromEntity) // Her bir Round'u DTO'ya çevir
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }
}