package com.korukmez.podium.platform.user.repository;

import com.korukmez.podium.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Spring'e bunun bir Repository olduğunu belirtir (best practice)
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA, metodun adından ne yapacağını anlar (Query Method)
    // "Email'e göre bir User bul"
    Optional<User> findByEmail(String email);

}