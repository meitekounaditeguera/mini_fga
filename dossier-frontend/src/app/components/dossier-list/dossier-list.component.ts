import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Dossier, IndemnisationStatut, RecouvrementStatut } from '../../models/dossier.model';
import { DossierService } from '../../services/dossier.service';
import { IndemnisationService } from '../../services/indemnisation.service';
import { RecouvrementService } from '../../services/recouvrement.service';
import { BanniereComponent } from '../banniere/banniere.component';

@Component({
  selector: 'app-dossier-list',
  standalone: true,
  imports: [CommonModule, FormsModule, BanniereComponent],
  templateUrl: './dossier-list.component.html',
  styleUrl: './dossier-list.component.css'
})
export class DossierListComponent implements OnInit {

  dossiers: Dossier[] = [];
  chargementEnCours = true;
  messageErreur: string | null = null;

  // On stocke maintenant l'OBJET COMPLET (id, statut, montant...),
  // plus seulement le statut - on a besoin de l'id de l'indemnisation
  // pour pouvoir la valider ensuite.
  indemnisations = new Map<string, IndemnisationStatut>();

  // Le recouvrement, obtenu depuis le 3e microservice (Python), une fois
  // l'indemnisation validée - clé : l'id de l'INDEMNISATION (pas du dossier).
  recouvrements = new Map<string, RecouvrementStatut>();

  // Stocke la saisie de l'utilisateur dans chaque petit formulaire de
  // validation, un par ligne du tableau - clé : id du dossier.
  montantsSaisis = new Map<string, number>();

  // Pour désactiver le bouton "Valider" pendant l'appel, et afficher un
  // message d'erreur spécifique à CETTE ligne si besoin.
  validationEnCours = new Set<string>();
  erreursValidation = new Map<string, string>();

  constructor(
    private dossierService: DossierService,
    private indemnisationService: IndemnisationService,
    private recouvrementService: RecouvrementService
  ) {
  }

  ngOnInit(): void {
    this.chargerDossiers();
  }

  chargerDossiers(): void {
    this.chargementEnCours = true;
    this.messageErreur = null;

    this.dossierService.listerDossiers().subscribe({
      next: (dossiers) => {
        this.dossiers = dossiers;
        this.chargementEnCours = false;
        this.chargerIndemnisations(dossiers);
      },
      error: (erreur) => {
        console.error('Erreur lors du chargement des dossiers', erreur);
        this.messageErreur = 'Impossible de charger les dossiers. Vérifie que sinistre-service tourne bien.';
        this.chargementEnCours = false;
      }
    });
  }

  private chargerIndemnisations(dossiers: Dossier[]): void {
    for (const dossier of dossiers) {
      this.indemnisationService.consulterParDossier(dossier.id).subscribe({
        next: (indemnisation) => {
          this.indemnisations.set(dossier.id, indemnisation);
          // Si elle est déjà validée (rechargement de page après coup),
          // on va aussi chercher son recouvrement.
          if (indemnisation.statut === 'VALIDEE') {
            this.chargerRecouvrement(indemnisation.id);
          }
        },
        error: () => {
          // 404 normal : Kafka n'a pas encore traité l'événement.
        }
      });
    }
  }

  private chargerRecouvrement(indemnisationId: string): void {
    this.recouvrementService.consulterParIndemnisation(indemnisationId).subscribe({
      next: (recouvrement) => {
        this.recouvrements.set(indemnisationId, recouvrement);
      },
      error: () => {
        // 404 normal ici aussi : recouvrement-service n'a peut-être pas
        // encore traité le 2e événement Kafka (indemnisation-validee).
      }
    });
  }

  indemnisationDe(dossier: Dossier): IndemnisationStatut | undefined {
    return this.indemnisations.get(dossier.id);
  }

  recouvrementDe(indemnisation: IndemnisationStatut): RecouvrementStatut | undefined {
    return this.recouvrements.get(indemnisation.id);
  }

  onMontantChange(dossierId: string, valeur: number): void {
    this.montantsSaisis.set(dossierId, valeur);
  }

  montantSaisiDe(dossierId: string): number | undefined {
    return this.montantsSaisis.get(dossierId);
  }

  estEnValidation(dossierId: string): boolean {
    return this.validationEnCours.has(dossierId);
  }

  erreurValidationDe(dossierId: string): string | undefined {
    return this.erreursValidation.get(dossierId);
  }

  valider(dossier: Dossier): void {
    const indemnisation = this.indemnisations.get(dossier.id);
    const montant = this.montantsSaisis.get(dossier.id);

    if (!indemnisation || !montant || montant <= 0) {
      this.erreursValidation.set(dossier.id, 'Saisis un montant valide avant de valider.');
      return;
    }

    this.validationEnCours.add(dossier.id);
    this.erreursValidation.delete(dossier.id);

    this.indemnisationService.validerIndemnisation(indemnisation.id, montant).subscribe({
      next: (indemnisationMiseAJour) => {
        this.indemnisations.set(dossier.id, indemnisationMiseAJour);
        this.validationEnCours.delete(dossier.id);
        // On relance une recherche de recouvrement après un court délai -
        // le temps que Kafka transmette l'événement à recouvrement-service.
        setTimeout(() => this.chargerRecouvrement(indemnisationMiseAJour.id), 1500);
      },
      error: (erreur) => {
        console.error('Erreur lors de la validation', erreur);
        const message = erreur?.error?.erreur ?? 'La validation a échoué.';
        this.erreursValidation.set(dossier.id, message);
        this.validationEnCours.delete(dossier.id);
      }
    });
  }
}