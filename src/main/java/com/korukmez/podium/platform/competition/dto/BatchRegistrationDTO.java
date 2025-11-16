package com.korukmez.podium.platform.competition.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchRegistrationDTO {

    @NotEmpty(message = "Kullanıcı ID listesi boş olamaz")
    private List<Long> userIds; // [1, 2, 5, 10] gibi bir ID listesi
}