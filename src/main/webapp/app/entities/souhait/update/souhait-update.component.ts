import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISouhait, Souhait } from '../souhait.model';
import { SouhaitService } from '../service/souhait.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { VoitureService } from 'app/entities/voiture/service/voiture.service';

@Component({
  selector: 'jhi-souhait-update',
  templateUrl: './souhait-update.component.html',
})
export class SouhaitUpdateComponent implements OnInit {
  isSaving = false;

  utilisateursCollection: IUtilisateur[] = [];
  voituresSharedCollection: IVoiture[] = [];

  editForm = this.fb.group({
    id: [],
    utilisateur: [],
    voitures: [],
  });

  constructor(
    protected souhaitService: SouhaitService,
    protected utilisateurService: UtilisateurService,
    protected voitureService: VoitureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ souhait }) => {
      this.updateForm(souhait);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const souhait = this.createFromForm();
    if (souhait.id !== undefined) {
      this.subscribeToSaveResponse(this.souhaitService.update(souhait));
    } else {
      this.subscribeToSaveResponse(this.souhaitService.create(souhait));
    }
  }

  trackUtilisateurById(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  trackVoitureById(index: number, item: IVoiture): number {
    return item.id!;
  }

  getSelectedVoiture(option: IVoiture, selectedVals?: IVoiture[]): IVoiture {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISouhait>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(souhait: ISouhait): void {
    this.editForm.patchValue({
      id: souhait.id,
      utilisateur: souhait.utilisateur,
      voitures: souhait.voitures,
    });

    this.utilisateursCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursCollection,
      souhait.utilisateur
    );
    this.voituresSharedCollection = this.voitureService.addVoitureToCollectionIfMissing(
      this.voituresSharedCollection,
      ...(souhait.voitures ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query({ filter: 'souhait-is-null' })
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.editForm.get('utilisateur')!.value)
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursCollection = utilisateurs));

    this.voitureService
      .query()
      .pipe(map((res: HttpResponse<IVoiture[]>) => res.body ?? []))
      .pipe(
        map((voitures: IVoiture[]) =>
          this.voitureService.addVoitureToCollectionIfMissing(voitures, ...(this.editForm.get('voitures')!.value ?? []))
        )
      )
      .subscribe((voitures: IVoiture[]) => (this.voituresSharedCollection = voitures));
  }

  protected createFromForm(): ISouhait {
    return {
      ...new Souhait(),
      id: this.editForm.get(['id'])!.value,
      utilisateur: this.editForm.get(['utilisateur'])!.value,
      voitures: this.editForm.get(['voitures'])!.value,
    };
  }
}
