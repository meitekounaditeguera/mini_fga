import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecouvrementStatut } from '../models/dossier.model';

/**
 * Un 3e service Angular, pour un 3e backend, sur un 3e port (8000).
 * Même principe strict que pour les deux autres : ce service ne connaît
 * QUE recouvrement-service, rien d'autre.
 */
@Injectable({
  providedIn: 'root'
})
export class RecouvrementService {

  private readonly apiUrl = 'http://localhost:8000/api/recouvrements';

  constructor(private http: HttpClient) {
  }

  consulterParIndemnisation(indemnisationId: string): Observable<RecouvrementStatut> {
    return this.http.get<RecouvrementStatut>(`${this.apiUrl}/${indemnisationId}`);
  }
}