import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CreerDossierRequest } from '../../models/dossier.model';
import { DossierService } from '../../services/dossier.service';
import { BanniereComponent } from '../banniere/banniere.component';

/**
 * FormsModule : nécessaire pour utiliser [(ngModel)] dans le template
 * (la liaison bidirectionnelle qu'on va voir dans le .html) - l'équivalent
 * Angular de spring-boot-starter-validation, mais pour les formulaires.
 *
 * @Output() : un mécanisme pour qu'un composant ENFANT (celui-ci)
 * prévienne son composant PARENT (app.component) qu'un événement s'est
 * produit - ici, "un dossier vient d'être créé, tu peux rafraîchir ta
 * liste". C'est le sens de communication inverse de @Input() (qui,
 * lui, ferait passer une donnée du parent vers l'enfant).
 */
@Component({
  selector: 'app-dossier-form',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    BanniereComponent
  ],

  templateUrl: './dossier-form.component.html',
  styleUrl: './dossier-form.component.css'
})
export class DossierFormComponent {

  nomVictime = '';
  dateAccident = '';
  lieu = '';
  envoiEnCours = false;
  messageErreur: string | null = null;
  messageSucces: string | null = null;

  // Ce composant "émet" un événement quand un dossier est créé avec
  // succès. app.component.ts va s'y "abonner" pour savoir quand
  // rafraîchir la liste des dossiers affichée à côté.
  @Output() dossierCree = new EventEmitter<void>();

  constructor(private dossierService: DossierService) {
  }

  soumettre(): void {
    this.envoiEnCours = true;
    this.messageErreur = null;
    this.messageSucces = null;

    const request: CreerDossierRequest = {
      nomVictime: this.nomVictime,
      dateAccident: this.dateAccident,
      lieu: this.lieu
    };

    this.dossierService.creerDossier(request).subscribe({
      next: (dossier) => {
        this.envoiEnCours = false;
        this.messageSucces = `Dossier ${dossier.numero} créé avec succès.`;
        this.reinitialiserFormulaire();
        // On prévient le composant parent : "un dossier a été créé".
        this.dossierCree.emit();
      },
      error: (erreur) => {
        console.error('Erreur lors de la création du dossier', erreur);
        this.messageErreur = 'La création a échoué. Vérifie les champs et que sinistre-service tourne bien.';
        this.envoiEnCours = false;
      }
    });
  }

  private reinitialiserFormulaire(): void {
    this.nomVictime = '';
    this.dateAccident = '';
    this.lieu = '';
  }
}