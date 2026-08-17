import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,

  imports: [
    CommonModule,
    RouterOutlet
  ],

  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Mini-FGA - Gestion des dossiers de sinistre';

  constructor(private authService: AuthService, private router: Router) {
  }

  get estConnecte(): boolean {
    return this.authService.estConnecte();
  }

  seDeconnecter(): void {
    this.authService.deconnecter();
    this.router.navigateByUrl('/login');
  }
}