package com.korukmez.podium.platform.security.controller;

import com.korukmez.podium.platform.security.dto.JwtAuthResponseDTO;
import com.korukmez.podium.platform.security.dto.LoginRequestDTO;
import com.korukmez.podium.platform.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * POST /api/auth/login
     * Kullanıcı girişi için endpoint. Başarılı girişte JWT döner.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        // 1. Spring Security ile kimlik doğrulama yap
        // Bu, PodiumUserDetailsService'i tetikler ve şifreyi kontrol eder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2. Güvenlik context'ine kimliği doğrulanmış kullanıcıyı ayarla
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. JWT Token oluştur
        String jwt = tokenProvider.generateToken(authentication);

        // 4. Token'ı response olarak dön
        return ResponseEntity.ok(new JwtAuthResponseDTO(jwt));
    }
}