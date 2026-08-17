package com.fga.erp.application.port.in;

import com.fga.erp.domain.EcritureComptable;
import java.util.List;

public interface ConsulterSyntheseUseCase {
    List<EcritureComptable> listerEcritures();
}
