import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

const CLE_TOKEN = 'fga_token';

interface LoginResponse {
  token: string;
}

/**
 * Appelle le vrai endpoint POST /api/auth/login de sinistre-service, qui
 * émet un JWT signé (compte de démo pour l'instant : admin/admin123,
 * aucune base utilisateurs n'existe encore côté backend). Ce même token
 * est ensuite accepté par indemnisation-service et recouvrement-service,
 * qui le vérifient avec le secret partagé - voir AuthInterceptor pour
 * l'ajout automatique de l'en-tête Authorization sur chaque appel HTTP.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) {
  }

  estConnecte(): boolean {
    return sessionStorage.getItem(CLE_TOKEN) !== null;
  }

  obtenirToken(): string | null {
    return sessionStorage.getItem(CLE_TOKEN);
  }

  connecter(utilisateur: string, motDePasse: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { utilisateur, motDePasse }).pipe(
      tap((reponse) => sessionStorage.setItem(CLE_TOKEN, reponse.token))
    );
  }

  deconnecter(): void {
    sessionStorage.removeItem(CLE_TOKEN);
  }
}
