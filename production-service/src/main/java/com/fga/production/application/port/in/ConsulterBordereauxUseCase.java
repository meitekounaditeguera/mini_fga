package com.fga.production.application.port.in;

import com.fga.production.domain.BordereauPrime;
import java.util.List;

public interface ConsulterBordereauxUseCase {
    List<BordereauPrime> listerBordereaux();
}
