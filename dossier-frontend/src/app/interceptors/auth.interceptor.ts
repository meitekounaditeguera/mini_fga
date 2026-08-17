import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * Ajoute automatiquement "Authorization: Bearer <token>" à chaque appel
 * HTTP sortant (vers sinistre-service, indemnisation-service ou
 * recouvrement-service), et redirige vers /login si un des trois répond
 * 401 (token manquant, invalide ou expiré).
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.obtenirToken();

  const requete = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(requete).pipe(
    catchError((erreur) => {
      if (erreur.status === 401) {
        authService.deconnecter();
        router.navigateByUrl('/login');
      }
      return throwError(() => erreur);
    })
  );
};
