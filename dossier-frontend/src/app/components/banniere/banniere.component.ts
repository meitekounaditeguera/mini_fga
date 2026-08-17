import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export type TypeBanniere = 'succes' | 'erreur' | 'attention';

/**
 * Bannière de message réutilisable (succès / erreur / attention), pour
 * remplacer les simples <p class="erreur"> disséminés dans l'app par
 * quelque chose de plus lisible et cohérent visuellement partout.
 */
@Component({
  selector: 'app-banniere',
  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './banniere.component.html',
  styleUrl: './banniere.component.css'
})
export class BanniereComponent {
  @Input() type: TypeBanniere = 'erreur';
  @Input() message: string | null = null;
}
