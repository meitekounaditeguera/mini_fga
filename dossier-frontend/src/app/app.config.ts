import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';

// provideHttpClient() : sans cette ligne, aucun service Angular ne peut
// utiliser HttpClient pour faire des appels vers sinistre-service.
// C'est l'équivalent Angular de "ajouter spring-boot-starter-web" côté
// Java - ça active la brique technique nécessaire pour parler HTTP.
//
// withInterceptors([authInterceptor]) : branche l'ajout automatique du
// token JWT sur CHAQUE appel HTTP sortant, vers les 3 microservices.
//
// provideRouter(routes) : active le routeur Angular à partir de la table
// définie dans app.routes.ts (page de login, page des dossiers protégée
// par un guard).
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    provideRouter(routes)
  ]
};
