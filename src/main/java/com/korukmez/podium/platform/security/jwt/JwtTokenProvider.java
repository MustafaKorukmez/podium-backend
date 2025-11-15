package com.korukmez.podium.platform.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMs;

    // application-dev.properties'den ayarları okur
    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKeyString,
                            @Value("${jwt.expiration-ms}") long expirationMs) {

        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Başarılı bir giriş (Authentication) sonrası JWT oluşturur.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        // Token'ın içine kullanıcının rollerini (authorities) ekliyoruz
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // "ROLE_JUDGE,ROLE_ADMIN"

        return Jwts.builder()
                .subject(userDetails.getUsername()) // Kullanıcının email'i
                .claim("roles", roles) // Özel alan: Roller
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Gelen token'dan email (username) bilgisini okur.
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Token'ı doğrular (imza ve süre kontrolü).
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            // Hata loglanabilir: Geçersiz imza, süresi dolmuş token vb.
            return false;
        }
    }
}