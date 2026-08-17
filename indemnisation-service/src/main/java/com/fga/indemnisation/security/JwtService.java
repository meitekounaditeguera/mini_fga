package com.fga.indemnisation.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Ce service ne fait que VÉRIFIER les tokens (signature + expiration) -
 * il n'émet jamais de token lui-même. La génération se fait uniquement
 * dans sinistre-service (POST /api/auth/login) ; les deux services
 * partagent le même secret pour que la vérification fonctionne sans
 * qu'ils aient besoin de s'appeler entre eux.
 */
@Component
public class JwtService {

    private final SecretKey cleSecrete;

    public JwtService(@Value("${fga.jwt.secret}") String secret) {
        this.cleSecrete = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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
