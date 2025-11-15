package com.korukmez.podium.platform.security.service;

import com.korukmez.podium.platform.user.entity.User;
import com.korukmez.podium.platform.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
public class PodiumUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PodiumUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security tarafından "login" işlemi sırasında çağrılır.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Kullanıcıyı email (kullanıcı adı olarak email kullanıyoruz) ile veritabanında bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email bulunamadı: " + email));

        // 2. Kullanıcının rolünü Spring Security'nin anlayacağı 'Authority' formatına çevir
        // Örn: "JUDGE" -> "ROLE_JUDGE" (Spring'in standardı 'ROLE_' önekidir)
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // 3. Spring Security'nin UserDetails nesnesini oluştur ve döndür
        // Bu nesne, Spring'in şifreleri karşılaştırması için kullanılır
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                 // (kullanıcı adı)
                user.getPasswordHash(),          // (veritabanındaki HASH'lenmiş şifre)
                Collections.singletonList(authority) // (kullanıcının rolleri)
        );
    }
}