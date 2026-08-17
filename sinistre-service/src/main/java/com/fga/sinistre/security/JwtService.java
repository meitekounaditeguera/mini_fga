package com.fga.sinistre.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Génère et vérifie les tokens JWT côté sinistre-service. C'est le SEUL
 * des 3 microservices qui génère des tokens (via /api/auth/login) - les
 * deux autres (indemnisation, recouvrement) se contentent de les vérifier
 * avec le même secret partagé, sans jamais appeler sinistre-service.
 */
@Component
public class JwtService {

    // Fixé explicitement en HS256 plutôt que de laisser jjwt choisir
    // automatiquement selon la taille de la clé (il aurait pris HS512
    // avec un secret aussi long) - indemnisation-service et
    // recouvrement-service vérifient tous les deux en HS256, il faut que
    // les 3 services soient alignés sur le même algorithme.
    private static final MacAlgorithm ALGORITHME = Jwts.SIG.HS256;

    private final SecretKey cleSecrete;
    private final long dureeValiditeMinutes;

    public JwtService(@Value("${fga.jwt.secret}") String secret,
                       @Value("${fga.jwt.expiration-minutes}") long dureeValiditeMinutes) {
        this.cleSecrete = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.dureeValiditeMinutes = dureeValiditeMinutes;
    }

    public String genererToken(String utilisateur) {
        Instant maintenant = Instant.now();

        return Jwts.builder()
                .subject(utilisateur)
                .issuedAt(Date.from(maintenant))
                .expiration(Date.from(maintenant.plus(dureeValiditeMinutes, ChronoUnit.MINUTES)))
                .signWith(cleSecrete, ALGORITHME)
                .compact();
    }

    public String extraireUtilisateur(String token) {
        return Jwts.parser()
                .verifyWith(cleSecrete)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
