package com.korukmez.podium.platform.competition.entity;

import com.korukmez.podium.platform.core.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "competitions")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT") // Uzun açıklamalar için
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // Varsayılan değer ataması

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- İlişkiler ---

    /**
     * Bu yarışmanın turları (Round).
     * mappedBy = "competition": Round sınıfındaki "competition" alanının bu ilişkinin
     * sahibi olduğunu belirtir. Veritabanında fazladan bir join tablosu oluşmasını engeller.
     * cascade = CascadeType.ALL: Bir Competition silinirse, ona ait TÜM Round'lar da silinir.
     * orphanRemoval = true: Bir Round'u bu listeden kaldırırsak, veritabanından da silinir.
     */
    @OneToMany(
            mappedBy = "competition",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY // Turları sadece ihtiyaç olduğunda (getTours()) yükle
    )
    private List<Round> rounds = new ArrayList<>(); // Null hatası almamak için listeyi başlat

    // CompetitionRegistration ilişkisi de buraya @OneToMany olarak eklenecek

    // ... Diğer alanlar (id, name, status, rounds listesi vs.) ...

    /**
     * Bu yarışmaya yapılan tüm başvurular (Jüriler + Yarışmacılar).
     */
    @OneToMany(
            mappedBy = "competition", // Registration'daki "competition" alanına bağlı
            cascade = CascadeType.ALL, // Competition silinirse tüm kayıtları silinsin
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CompetitionRegistration> registrations = new ArrayList<>();
}