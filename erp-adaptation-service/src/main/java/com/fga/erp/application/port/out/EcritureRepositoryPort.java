package com.fga.erp.application.port.out;

import com.fga.erp.domain.EcritureComptable;
import java.util.List;

public interface EcritureRepositoryPort {
    EcritureComptable sauvegarder(EcritureComptable ecriture);
    List<EcritureComptable> listerToutes();
}
