package com.korukmez.podium.platform.competition.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList; // Gerekli import
import java.util.List;      // Gerekli import

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "performance_groups")
public class PerformanceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Örn: "Grup A", "Grup B"

    /**
     * Bu sahne grubunun ait olduğu Tur (Round).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    // --- İlişkiler ---

    /**
     * Bu grupta yer alan yarışmacıların üyelik kayıtları.
     */
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<GroupMembership> memberships = new ArrayList<>();
}