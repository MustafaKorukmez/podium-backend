package com.korukmez.podium.platform.competition.repository;

import com.korukmez.podium.platform.competition.entity.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GropMembershipRepository extends JpaRepository<GroupMembership,Long> {
}
