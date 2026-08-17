import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IndemnisationStatut } from '../models/dossier.model';

@Injectable({
  providedIn: 'root'
})
export class IndemnisationService {

  private readonly apiUrl = 'http://localhost:8082/api/indemnisations';

  constructor(private http: HttpClient) {
  }

  consulterParDossier(dossierId: string): Observable<IndemnisationStatut> {
    return this.http.get<IndemnisationStatut>(`${this.apiUrl}/dossier/${dossierId}`);
  }

   // Nouvelle méthode : appelle PUT /api/indemnisations/{id}/valider,
  // le endpoint qu'on a construit côté Java pour déclencher toute la
  // chaîne (validation -> Kafka -> recouvrement-service).
  validerIndemnisation(indemnisationId: string, montant: number): Observable<IndemnisationStatut> {
    return this.http.put<IndemnisationStatut>(
      `${this.apiUrl}/${indemnisationId}/valider`,
      { montant }
    );
  }
}