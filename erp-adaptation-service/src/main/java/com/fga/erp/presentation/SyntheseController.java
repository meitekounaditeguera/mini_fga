package com.fga.erp.presentation;

import com.fga.erp.application.port.in.ConsulterSyntheseUseCase;
import com.fga.erp.domain.EcritureComptable;
import com.fga.erp.domain.TypeEcriture;
import com.fga.erp.presentation.dto.EcritureResponse;
import com.fga.erp.presentation.dto.SyntheseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/synthese")
public class SyntheseController {

    private final ConsulterSyntheseUseCase consulterUseCase;

    public SyntheseController(ConsulterSyntheseUseCase consulterUseCase) {
        this.consulterUseCase = consulterUseCase;
    }

    @GetMapping
    public SyntheseResponse consulter() {
        List<EcritureComptable> ecritures = consulterUseCase.listerEcritures();

        BigDecimal totalPrimes = sommerParType(ecritures, TypeEcriture.PRIME_RECUE);
        BigDecimal totalIndemnisations = sommerParType(ecritures, TypeEcriture.INDEMNISATION_VERSEE);

        return new SyntheseResponse(
                ecritures.stream().map(EcritureResponse::depuis).toList(),
                totalPrimes,
                totalIndemnisations,
                totalPrimes.subtract(totalIndemnisations)
        );
    }

    private BigDecimal sommerParType(List<EcritureComptable> ecritures, TypeEcriture type) {
        return ecritures.stream()
                .filter(e -> e.getType() == type)
                .map(EcritureComptable::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
