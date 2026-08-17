import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreerDossierRequest, Dossier } from '../models/dossier.model';

/**
 * L'équivalent, côté Angular, de DossierController.java côté back-end -
 * mais dans l'autre sens : ici, on APPELLE l'API REST, on ne la sert pas.
 *
 * @Injectable : l'équivalent Angular de @Service côté Spring. Ça dit à
 * Angular "gère cette classe toi-même, crée-la une seule fois, et
 * injecte-la partout où un composant en a besoin".
 */
@Injectable({
  providedIn: 'root'
})
export class DossierService {

  // L'adresse de sinistre-service. En dur ici pour rester simple - dans un
  // vrai projet d'entreprise, cette URL viendrait d'un fichier de
  // configuration (environment.ts), pour changer facilement entre
  // développement et production.
  private readonly apiUrl = 'http://localhost:8081/api/dossiers';

  constructor(private http: HttpClient) {
  }

  /**
   * Équivalent de : GET http://localhost:8081/api/dossiers
   * Le type Observable<Dossier[]> annonce : "ceci renverra, plus tard
   * (de façon asynchrone), un tableau de Dossier".
   */
  listerDossiers(): Observable<Dossier[]> {
    return this.http.get<Dossier[]>(this.apiUrl);
  }

  /**
   * Équivalent de : POST http://localhost:8081/api/dossiers
   * avec le corps JSON de la requête.
   */
  creerDossier(request: CreerDossierRequest): Observable<Dossier> {
    return this.http.post<Dossier>(this.apiUrl, request);
  }
}
