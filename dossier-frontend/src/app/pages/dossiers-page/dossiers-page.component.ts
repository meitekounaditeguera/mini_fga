import { Component, ViewChild } from '@angular/core';
import { DossierListComponent } from '../../components/dossier-list/dossier-list.component';
import { DossierFormComponent } from '../../components/dossier-form/dossier-form.component';

@Component({
  selector: 'app-dossiers-page',
  standalone: true,

  imports: [
    DossierListComponent,
    DossierFormComponent
  ],

  templateUrl: './dossiers-page.component.html',
  styleUrl: './dossiers-page.component.css'
})
export class DossiersPageComponent {

  @ViewChild(DossierListComponent) dossierListComponent!: DossierListComponent;

  onDossierCree(): void {
    this.dossierListComponent.chargerDossiers();
  }
}
