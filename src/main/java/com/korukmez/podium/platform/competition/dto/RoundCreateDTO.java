package com.korukmez.podium.platform.competition.dto;

import com.korukmez.podium.platform.core.enums.ScoringType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundCreateDTO {

    @NotEmpty(message = "Tur adı boş olamaz")
    private String name; // Örn: "Eleme Turu"

    @NotNull
    @Min(value = 1, message = "Tur sırası en az 1 olmalıdır")
    private Integer roundOrder; // 1, 2, 3...

    @NotNull
    @Min(value = 1, message = "Grup büyüklüğü en az 1 olmalıdır")
    private Integer groupSize; // 10

    @NotNull
    @Min(value = 1, message = "İlerleyecek kişi sayısı en az 1 olmalıdır")
    private Integer participantsToAdvance; // 40

    @NotNull(message = "Puanlama tipi (SCORING/RANKING) zorunludur")
    private ScoringType scoringType;
}