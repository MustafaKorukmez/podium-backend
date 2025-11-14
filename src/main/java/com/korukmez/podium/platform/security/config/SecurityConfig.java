package com.korukmez.podium.platform.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer; // Bu import'a dikkat
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bu güvenlik filtresi SADECE "dev" profili (H2 veritabanı) aktifken çalışır.
     * Canlı (prod) ortamda H2 Console'a erişim ASLA olmamalıdır.
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // H2 Console'a gelen TÜM isteklere izin ver
                        .requestMatchers("/h2-console/**").permitAll()

                        // Entity'leri test ettiğimiz bu aşamada, şimdilik diğer her şeye de izin verelim
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf
                        // H2 Console, CSRF token'ı kullanmaz, bu yüzden o yol için kapatıyoruz
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        // H2 Console'un frame içinde çalışabilmesi için bu ayar şart
                        // HATALI SATIR: .frameOptions(HeadersConfigurer.FrameOptionsConfig::SAMEORIGIN)
                        // DÜZELTME: Metod adı 'sameOrigin' (küçük 's' ile) olmalı
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }

    /**
     * Buraya daha sonra canlı (production) ortam için
     * @Profile("prod") ile ikinci bir filter chain eklenecek.
     * O chain H2'ye İZİN VERMEYECEK ve her şeyi koruyacak.
     */

}