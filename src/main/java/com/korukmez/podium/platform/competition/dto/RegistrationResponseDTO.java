package com.korukmez.podium.platform.competition.dto;

import com.korukmez.podium.platform.competition.entity.CompetitionRegistration;
import com.korukmez.podium.platform.core.enums.RegistrationStatus;
import com.korukmez.podium.platform.user.dto.UserResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationResponseDTO {

    private Long id; // Kaydın (registration) ID'si
    private RegistrationStatus status;
    private UserResponseDTO user; // Hangi kullanıcının kaydedildiği
    private Long competitionId; // Hangi yarışmaya kaydedildiği

    public static RegistrationResponseDTO fromEntity(CompetitionRegistration registration) {
        RegistrationResponseDTO dto = new RegistrationResponseDTO();
        dto.setId(registration.getId());
        dto.setStatus(registration.getStatus());
        dto.setUser(UserResponseDTO.fromEntity(registration.getUser()));
        dto.setCompetitionId(registration.getCompetition().getId());
        return dto;
    }
}