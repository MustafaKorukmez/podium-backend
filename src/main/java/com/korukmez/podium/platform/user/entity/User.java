package com.korukmez.podium.platform.user.entity;

// Gerekli import'lar
import com.korukmez.podium.platform.core.enums.UserRole;
import com.korukmez.podium.platform.competition.entity.CompetitionRegistration; // <-- DÜZELTME BURADA
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList; // Listeyi başlatmak için eklendi
import java.util.List;      // List tipi için eklendi

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- İlişkiler ---

    /**
     * Bu kullanıcının yaptığı tüm yarışma kayıtları.
     */
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CompetitionRegistration> registrations = new ArrayList<>();
}