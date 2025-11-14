package com.korukmez.podium.platform.competition.entity;

import com.korukmez.podium.platform.user.entity.User; // Gerekli import
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "group_memberships", uniqueConstraints = {
        // Bir yarışmacı bir gruba sadece 1 kez üye olabilir
        @UniqueConstraint(columnNames = {"group_id", "contestant_user_id"})
})
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Üyesi olunan grup.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private PerformanceGroup group;

    /**
     * Gruba üye olan Yarışmacı (Contestant rolündeki User).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contestant_user_id", nullable = false)
    private User contestant; // 'User' entity'sine bağlanır
}