package com.korukmez.podium.platform.competition.entity;

// Kendi paketlerimizden importlar
import com.korukmez.podium.platform.core.enums.RegistrationStatus;
import com.korukmez.podium.platform.user.entity.User;

// Spring & JPA importları
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "competition_registrations", uniqueConstraints = {
        // Bir kullanıcı (user_id) bir yarışmaya (competition_id) SADECE 1 kez kaydolabilir.
        @UniqueConstraint(columnNames = {"user_id", "competition_id"})
})
public class CompetitionRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Kaydolan Kullanıcı (Jüri veya Yarışmacı).
     * User'ın rolünün ne olduğu (JUDGE/CONTESTANT) bu entity'yi ilgilendirmez,
     * Sadece kaydı yapan User'ı tutar.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Kayıt yapılan Yarışma.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}