package com.fga.sinistre.presentation;

import com.fga.sinistre.application.port.in.ConsulterDossiersUseCase;
import com.fga.sinistre.application.port.in.CreerDossierUseCase;
import com.fga.sinistre.domain.Dossier;
import com.fga.sinistre.presentation.dto.CreerDossierRequest;
import com.fga.sinistre.presentation.dto.DossierResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Le "port d'entrée REST". Il ne connaît que les interfaces (use cases),
 * jamais l'implémentation concrète (DossierService) : c'est Spring qui
 * injecte la bonne implémentation grâce à @Service.
 *
 * Le controller ne contient AUCUNE règle métier : juste la traduction
 * HTTP <-> use case.
 */
@RestController
@RequestMapping("/api/dossiers")
public class DossierController {

    private final CreerDossierUseCase creerDossierUseCase;
    private final ConsulterDossiersUseCase consulterDossiersUseCase;

    public DossierController(CreerDossierUseCase creerDossierUseCase,
                              ConsulterDossiersUseCase consulterDossiersUseCase) {
        this.creerDossierUseCase = creerDossierUseCase;
        this.consulterDossiersUseCase = consulterDossiersUseCase;
    }

    @PostMapping
    public ResponseEntity<DossierResponse> creerDossier(@Valid @RequestBody CreerDossierRequest request) {
        Dossier dossier = creerDossierUseCase.creerDossier(
                request.getNomVictime(),
                request.getDateAccident(),
                request.getLieu()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(DossierResponse.depuis(dossier));
    }

    @GetMapping
    public List<DossierResponse> listerDossiers() {
        return consulterDossiersUseCase.listerDossiers().stream()
                .map(DossierResponse::depuis)
                .toList();
    }

    @GetMapping("/{id}")
    public DossierResponse consulterDossier(@PathVariable UUID id) {
        return DossierResponse.depuis(consulterDossiersUseCase.consulterDossier(id));
    }
}
