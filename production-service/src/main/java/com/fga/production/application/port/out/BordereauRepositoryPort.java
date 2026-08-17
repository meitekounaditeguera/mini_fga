package com.fga.production.application.port.out;

import com.fga.production.domain.BordereauPrime;
import java.util.List;

public interface BordereauRepositoryPort {
    BordereauPrime sauvegarder(BordereauPrime bordereau);
    List<BordereauPrime> listerTous();
}
