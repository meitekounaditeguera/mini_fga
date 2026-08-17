package com.fga.sinistre.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "L'identifiant est obligatoire")
    private String utilisateur;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    public LoginRequest() {
    }

    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
