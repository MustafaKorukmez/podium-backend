package com.korukmez.podium.platform.security.jwt;

import com.korukmez.podium.platform.security.service.PodiumUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final PodiumUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, PodiumUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Request'ten JWT'yi çek
            String jwt = getJwtFromRequest(request);

            // 2. Token'ı doğrula
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. Token'dan email'i al
                String email = tokenProvider.getEmailFromToken(jwt);

                // 4. Email ile kullanıcıyı veritabanından yükle
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 5. Kullanıcıyı Spring Security Context'ine "giriş yapmış" olarak ayarla
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Hata loglanabilir
        }

        // 6. İsteğin devam etmesine izin ver
        filterChain.doFilter(request, response);
    }

    // "Authorization: Bearer <token>" header'ından token'ı ayıklar
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}