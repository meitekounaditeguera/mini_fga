package com.fga.erp.infrastructure.persistence;

import com.fga.erp.application.port.out.EcritureRepositoryPort;
import com.fga.erp.domain.EcritureComptable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EcritureRepositoryAdapter implements EcritureRepositoryPort {

    private final EcritureJpaRepository jpaRepository;

    public EcritureRepositoryAdapter(EcritureJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public EcritureComptable sauvegarder(EcritureComptable ecriture) {
        EcritureEntity entity = new EcritureEntity(ecriture.getId(), ecriture.getType(),
                ecriture.getReference(), ecriture.getMontant(), ecriture.getDateEcriture());
        EcritureEntity sauvegarde = jpaRepository.save(entity);
        return versDomaine(sauvegarde);
    }

    @Override
    public List<EcritureComptable> listerToutes() {
        return jpaRepository.findAll().stream().map(this::versDomaine).toList();
    }

    private EcritureComptable versDomaine(EcritureEntity entity) {
        return EcritureComptable.reconstituer(entity.getId(), entity.getType(),
                entity.getReference(), entity.getMontant(), entity.getDateEcriture());
    }
}
