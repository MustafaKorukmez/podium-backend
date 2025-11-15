package com.korukmez.podium.platform.user.dto;

import com.korukmez.podium.platform.core.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @NotEmpty(message = "İsim alanı boş olamaz")
    private String fullName;

    @Email(message = "Geçerli bir email adresi giriniz")
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String password;

    @NotNull(message = "Kullanıcı rolü (JUDGE, CONTESTANT) zorunludur")
    private UserRole role;
}