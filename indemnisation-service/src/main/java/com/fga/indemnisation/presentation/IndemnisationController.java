package com.fga.indemnisation.presentation;

import com.fga.indemnisation.application.port.in.ConsulterIndemnisationParDossierUseCase;
import com.fga.indemnisation.application.port.in.ValiderIndemnisationUseCase;
import com.fga.indemnisation.domain.Indemnisation;
import com.fga.indemnisation.presentation.dto.IndemnisationResponse;
import com.fga.indemnisation.presentation.dto.ValiderIndemnisationRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/indemnisations")
public class IndemnisationController {

    private final ConsulterIndemnisationParDossierUseCase consulterUseCase;
    private final ValiderIndemnisationUseCase validerUseCase;

    public IndemnisationController(ConsulterIndemnisationParDossierUseCase consulterUseCase,
                                    ValiderIndemnisationUseCase validerUseCase) {
        this.consulterUseCase = consulterUseCase;
        this.validerUseCase = validerUseCase;
    }

    @GetMapping("/dossier/{dossierId}")
    public IndemnisationResponse consulterParDossier(@PathVariable UUID dossierId) {
        Indemnisation indemnisation = consulterUseCase.consulterParDossierId(dossierId);
        return IndemnisationResponse.depuis(indemnisation);
    }

    // PUT /api/indemnisations/{id}/valider
    // C'est ce nouvel endpoint qu'on va appeler (via curl/Postman d'abord,
    // pour tester) afin de déclencher toute la chaîne : validation du
    // domaine -> sauvegarde -> publication Kafka -> recouvrement-service.
    @PutMapping("/{id}/valider")
    public IndemnisationResponse validerIndemnisation(@PathVariable UUID id,
                                                        @Valid @RequestBody ValiderIndemnisationRequest request) {
        Indemnisation indemnisation = validerUseCase.validerIndemnisation(id, request.getMontant());
        return IndemnisationResponse.depuis(indemnisation);
    }
}