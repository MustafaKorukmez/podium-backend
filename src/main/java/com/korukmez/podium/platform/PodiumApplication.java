package com.korukmez.podium.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // <-- 1. IMPORT
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // <-- 2. IMPORT

@SpringBootApplication
@EntityScan(basePackages = {
		"com.korukmez.podium.platform.user.entity",
		"com.korukmez.podium.platform.competition.entity",
		"com.korukmez.podium.platform.scoring.entity"
}) // <-- 3. TÜM ENTITY PAKETLERİMİZİ EKLE
@EnableJpaRepositories(basePackages = {
		"com.korukmez.podium.platform.user.repository",
		"com.korukmez.podium.platform.competition.repository",
		"com.korukmez.podium.platform.scoring.repository"
}) // <-- 4. TÜM REPOSITORY PAKETLERİMİZİ EKLE
public class PodiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(PodiumApplication.class, args);
	}

}