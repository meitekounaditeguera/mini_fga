package com.fga.production.application.port.in;

import com.fga.production.domain.BordereauPrime;
import java.math.BigDecimal;

public interface ReceptionnerBordereauUseCase {
    BordereauPrime receptionnerBordereau(String compagnieAssurance, BigDecimal montantPrimesCollectees);
}
