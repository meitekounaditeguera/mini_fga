import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { BanniereComponent } from '../banniere/banniere.component';

@Component({
  selector: 'app-login',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    BanniereComponent
  ],

  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  utilisateur = '';
  motDePasse = '';
  connexionEnCours = false;
  messageErreur: string | null = null;

  constructor(private authService: AuthService, private router: Router) {
  }

  soumettre(): void {
    this.connexionEnCours = true;
    this.messageErreur = null;

    this.authService.connecter(this.utilisateur, this.motDePasse).subscribe({
      next: () => {
        this.connexionEnCours = false;
        this.router.navigateByUrl('/');
      },
      error: (erreur) => {
        console.error('Erreur de connexion', erreur);
        this.connexionEnCours = false;
        this.messageErreur = erreur.status === 401
          ? 'Identifiants incorrects. Réessaie.'
          : 'Connexion à sinistre-service impossible. Vérifie qu\'il tourne bien.';
      }
    });
  }
}
