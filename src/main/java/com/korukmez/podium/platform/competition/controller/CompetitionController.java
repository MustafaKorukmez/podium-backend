package com.korukmez.podium.platform.competition.controller;

import com.korukmez.podium.platform.competition.dto.CompetitionCreateDTO;
import com.korukmez.podium.platform.competition.dto.CompetitionResponseDTO;
import com.korukmez.podium.platform.competition.entity.Competition; // DTO'ya dönüştüreceğiz
import com.korukmez.podium.platform.competition.service.CompetitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
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
}