package com.korukmez.podium.platform.competition.controller;

import com.korukmez.podium.platform.competition.dto.BatchRegistrationDTO;
import com.korukmez.podium.platform.competition.dto.CompetitionCreateDTO;
import com.korukmez.podium.platform.competition.dto.CompetitionResponseDTO;
import com.korukmez.podium.platform.competition.dto.RegistrationResponseDTO;
import com.korukmez.podium.platform.competition.entity.Competition; // DTO'ya dönüştüreceğiz
import com.korukmez.podium.platform.competition.entity.CompetitionRegistration;
import com.korukmez.podium.platform.competition.service.CompetitionService;
import com.korukmez.podium.platform.competition.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;
    private final RegistrationService registrationService;

    public CompetitionController(CompetitionService competitionService, RegistrationService registrationService) {
        this.competitionService = competitionService;
        this.registrationService = registrationService;
    }

    /**
     * POST /api/competitions
     * Yeni bir yarışma (turlarıyla birlikte) oluşturur.
     * Sadece ADMIN rolü yapabilir.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompetitionResponseDTO> createCompetition(@Valid @RequestBody CompetitionCreateDTO dto) {

        CompetitionResponseDTO newCompetitionDTO = competitionService.createCompetition(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCompetitionDTO);
    }

    /**
     * POST /api/competitions/{competitionId}/register/{userId}
     * Bir kullanıcıyı (Jüri/Yarışmacı) bir yarışmaya kaydeder (başvuru).
     * Bu işlemi ADMIN yapabilir. (veya ileride kullanıcının kendisi)
     */
    @PostMapping("/{competitionId}/register/{userId}")
    @PreAuthorize("hasRole('ADMIN')") // Şimdilik sadece Admin kayıt yapsın
    public ResponseEntity<RegistrationResponseDTO> registerUserToCompetition(
            @PathVariable Long competitionId,
            @PathVariable Long userId) {

        RegistrationResponseDTO registration = registrationService.registerUserToCompetition(userId, competitionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }

    /**
     * POST /api/competitions/{competitionId}/register
     * Bir ID listesini (body) toplu olarak bir yarışmaya kaydeder.
     */
    @PostMapping("/{competitionId}/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegistrationResponseDTO>> registerBatch(
            @PathVariable Long competitionId,
            @Valid @RequestBody BatchRegistrationDTO dto) {

        List<RegistrationResponseDTO> registrations =
                registrationService.registerUsersInBatch(dto.getUserIds(), competitionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrations);
    }
}