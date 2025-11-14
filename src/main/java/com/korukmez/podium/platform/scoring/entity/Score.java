package com.korukmez.podium.platform.scoring.entity;

import com.korukmez.podium.platform.competition.entity.Round;
import com.korukmez.podium.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scores", uniqueConstraints = {
        // Bir jüri, bir turda, bir yarışmacıya SADECE 1 puan verebilir
        @UniqueConstraint(columnNames = {"round_id", "judge_user_id", "contestant_user_id"})
})
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- SORUNUN ÇÖZÜMÜ BU SATIR
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_user_id", nullable = false)
    private User judge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contestant_user_id", nullable = false)
    private User contestant;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal scoreValue;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}