package com.fga.sinistre.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) : ce que le client Angular envoie en JSON.
 * On ne fait JAMAIS transiter le domaine directement dans les controllers,
 * pour ne pas coupler l'API REST à la structure interne du domaine.
 */
public class CreerDossierRequest {

    @NotBlank(message = "Le nom de la victime est obligatoire")
    private String nomVictime;

    @NotNull(message = "La date de l'accident est obligatoire")
    @PastOrPresent(message = "La date de l'accident ne peut pas être dans le futur")
    private LocalDate dateAccident;

    private String lieu;

    public CreerDossierRequest() {
    }

    public String getNomVictime() { return nomVictime; }
    public void setNomVictime(String nomVictime) { this.nomVictime = nomVictime; }

    public LocalDate getDateAccident() { return dateAccident; }
    public void setDateAccident(LocalDate dateAccident) { this.dateAccident = dateAccident; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
}
