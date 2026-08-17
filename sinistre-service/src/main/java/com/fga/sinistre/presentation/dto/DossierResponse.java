package com.fga.sinistre.presentation.dto;

import com.fga.sinistre.domain.Dossier;
import com.fga.sinistre.domain.StatutDossier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de sortie : ce que l'API renvoie en JSON au client.
 */
public class DossierResponse {

    private UUID id;
    private String numero;
    private String nomVictime;
    private LocalDate dateAccident;
    private String lieu;
    private StatutDossier statut;
    private LocalDateTime dateCreation;

    public static DossierResponse depuis(Dossier dossier) {
        DossierResponse response = new DossierResponse();
        response.id = dossier.getId();
        response.numero = dossier.getNumero();
        response.nomVictime = dossier.getNomVictime();
        response.dateAccident = dossier.getDateAccident();
        response.lieu = dossier.getLieu();
        response.statut = dossier.getStatut();
        response.dateCreation = dossier.getDateCreation();
        return response;
    }

    public UUID getId() { return id; }
    public String getNumero() { return numero; }
    public String getNomVictime() { return nomVictime; }
    public LocalDate getDateAccident() { return dateAccident; }
    public String getLieu() { return lieu; }
    public StatutDossier getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
}
