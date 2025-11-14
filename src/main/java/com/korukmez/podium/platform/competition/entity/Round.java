package com.korukmez.podium.platform.competition.entity;


import com.korukmez.podium.platform.competition.entity.PerformanceGroup;
import com.korukmez.podium.platform.core.enums.ScoringType;
import com.korukmez.podium.platform.core.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rounds", uniqueConstraints = {
        // Bir yarışma içinde aynı 'round_order' (tur sırası) olamaz
        @UniqueConstraint(columnNames = {"competition_id", "round_order"})
})
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "round_order", nullable = false)
    private int roundOrder; // Tur sırası (1, 2, 3...)

    // --- Dinamik Kurallar ---
    @Column(name = "group_size", nullable = false)
    private int groupSize; // Bu turdaki grup büyüklüğü

    @Column(name = "participants_to_advance", nullable = false)
    private int participantsToAdvance; // Bir sonraki tura kaç kişi geçecek

    @Enumerated(EnumType.STRING)
    @Column(name = "scoring_type", nullable = false)
    private ScoringType scoringType; // Puanlama mı, Sıralama mı?

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // Varsayılan değer

    // --- İlişkiler ---

    /**
     * Bu turun ait olduğu yarışma (Competition).
     * @ManyToOne: Birçok tur BİR yarışmaya aittir.
     * nullable = false: Hiçbir tur yarışmasız (boşta) olamaz.
     * JoinColumn: Yabancı anahtar (foreign key) sütununun adını belirtir.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    // PerformanceGroup ilişkisi buraya @OneToMany olarak eklenecek
    /**
     * Bu tura ait tüm sahne gruplarının (Grup A, B, C...) listesi.
     */
    @OneToMany(
            mappedBy = "round",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PerformanceGroup> performanceGroups = new ArrayList<>();
}