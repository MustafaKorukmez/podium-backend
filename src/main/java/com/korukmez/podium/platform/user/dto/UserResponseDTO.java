package com.korukmez.podium.platform.user.dto;

import com.korukmez.podium.platform.core.enums.UserRole;
import com.korukmez.podium.platform.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private UserRole role;

    /**
     * Entity'yi DTO'ya dönüştüren fabrika metodu (Best Practice).
     */
    public static UserResponseDTO fromEntity(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}