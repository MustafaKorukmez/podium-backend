package com.korukmez.podium.platform.competition.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompetitionCreateDTO {

    @NotEmpty(message = "Yarışma adı boş olamaz")
    private String name;

    private String description;

    @Valid // Bu, iç içe DTO'ların da (@NotEmpty vb.) doğrulanmasını sağlar
    @NotEmpty(message = "Bir yarışmanın en az bir turu olmalıdır")
    @Size(min = 1)
    private List<RoundCreateDTO> rounds; // Turların listesi
}