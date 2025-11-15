package com.korukmez.podium.platform.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotEmpty(message = "Email boş olamaz")
    @Email
    private String email;

    @NotEmpty(message = "Şifre boş olamaz")
    private String password;
}