package com.korukmez.podium.platform.security.config;

import com.korukmez.podium.platform.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- YENİ IMPORT
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <-- YENİ IMPORT

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthController'da ihtiyaç duyduğumuz AuthenticationManager'ı @Bean olarak sunar.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * NOT: Artık @Profile("dev") YOK. Bu, hem dev hem prod için ana ayarımız olacak.
     * JWT, H2 Console ve API kurallarını birleştireceğiz.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF'i kapat (API'ler için standart)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. OTURUM YÖNETİMİ: STATELESS (DURUMSUZ) YAP
                // Spring Security'nin session (oturum) oluşturmasını engeller.
                // Bu, her isteğin token ile doğrulanmasını zorunlu kılar.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. ENDPOINT YETKİLERİ
                .authorizeHttpRequests(authz -> authz
                        // H2 Console'a (sadece dev'de açık olmalı) izin ver
                        .requestMatchers("/h2-console/**").permitAll()
                        // Login endpoint'ine izin ver
                        .requestMatchers("/api/auth/login").permitAll()
                        // Yeni kullanıcı oluşturma endpoint'ine izin ver
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // Yeni kullanıcı oluşturma (TOPLU) endpoint'ine izin ver
                        .requestMatchers(HttpMethod.POST, "/api/users/batch").permitAll()

                        // --- KURAL GÜNCELLEMESİ ---
                        // Yukarıdakiler dışında kalan TÜM istekler kimlik doğrulaması (JWT Token) gerektirir.
                        .anyRequest().authenticated()
                )

                // 4. H2 Console'un frame'lerde çalışmasına izin ver
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                // 5. KENDİ FİLTREMİZİ EKLE
                // Spring'in normal filtre zincirine, Username/Password filtresinden ÖNCE
                // bizim JWT filtremizi ekle.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}