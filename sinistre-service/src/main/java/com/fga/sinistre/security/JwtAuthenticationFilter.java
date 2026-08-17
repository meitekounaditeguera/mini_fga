package com.fga.sinistre.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * S'exécute avant chaque requête HTTP : lit l'en-tête "Authorization",
 * vérifie le token JWT (signature + expiration), et, s'il est valide,
 * authentifie la requête auprès de Spring Security. Aucune route de
 * connexion ici - juste de la vérification, exactement comme dans
 * indemnisation-service et recouvrement-service.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXE_BEARER = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        String enTeteAuthorization = request.getHeader("Authorization");

        if (enTeteAuthorization != null && enTeteAuthorization.startsWith(PREFIXE_BEARER)) {
            String token = enTeteAuthorization.substring(PREFIXE_BEARER.length());

            try {
                String utilisateur = jwtService.extraireUtilisateur(token);
                var authentication = new UsernamePasswordAuthenticationToken(utilisateur, null, List.of());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException e) {
                // Token invalide, altéré ou expiré : on n'authentifie pas la
                // requête. SecurityConfig la rejettera avec un 401 si la
                // route demandée exige d'être authentifié.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
