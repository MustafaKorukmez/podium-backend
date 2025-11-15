package com.korukmez.podium.platform.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // <-- YENİ IMPORT
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bu güvenlik filtresi SADECE "dev" profili (H2 veritabanı) aktifken çalışır.
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll() // H2 Console'a izin ver
                        .requestMatchers("/api/**").permitAll() // /api altındaki her şeye izin ver
                        .anyRequest().permitAll() // Geri kalan her şeye de izin ver
                )

                // --- DÜZELTME BURADA ---
                // "dev" profilinde CSRF'i tamamen kapatarak Postman isteklerine izin ver
                .csrf(AbstractHttpConfigurer::disable)

                .headers(headers -> headers
                        // H2 Console'un frame içinde çalışabilmesi için bu ayar şart
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }
}