package com.korukmez.podium.platform.user.repository;

import com.korukmez.podium.platform.core.enums.UserRole;
import com.korukmez.podium.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository // Spring'e bunun bir Repository olduğunu belirtir (best practice)
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA, metodun adından ne yapacağını anlar (Query Method)
    // "Email'e göre bir User bul"
    Optional<User> findByEmail(String email);

    /**
     * Verilen role sahip tüm kullanıcıları bulan Spring Data JPA sorgusu.
     * Örn: findAllByRole(UserRole.JUDGE)
     */
    List<User> findAllByRole(UserRole role);

    /**
     * Verilen email listesindeki (Set) veritabanında zaten var olan
     * tüm kullanıcıları döndürür.
     */
    List<User> findAllByEmailIn(Set<String> emails);

}