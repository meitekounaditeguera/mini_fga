import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DossiersPageComponent } from './pages/dossiers-page/dossiers-page.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: DossiersPageComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
