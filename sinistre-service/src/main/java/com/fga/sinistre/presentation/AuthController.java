package com.fga.sinistre.presentation;

import com.fga.sinistre.presentation.dto.LoginRequest;
import com.fga.sinistre.presentation.dto.LoginResponse;
import com.fga.sinistre.security.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Seul endpoint public du microservice. Aucune base utilisateurs pour
 * l'instant (projet d'entrainement) : un seul compte de démonstration,
 * le même que celui utilisé côté front en attendant. Le jour où une vraie
 * gestion des utilisateurs existe, seule cette méthode login() change -
 * le reste (JwtService, filtre, SecurityConfig) ne bouge pas.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String UTILISATEUR_DEMO = "admin";
    private static final String MOT_DE_PASSE_DEMO = "admin123";

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        boolean identifiantsValides = UTILISATEUR_DEMO.equals(request.getUtilisateur())
                && MOT_DE_PASSE_DEMO.equals(request.getMotDePasse());

        if (!identifiantsValides) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtService.genererToken(request.getUtilisateur())));
    }
}
