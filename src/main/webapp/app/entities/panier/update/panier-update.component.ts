import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPanier, Panier } from '../panier.model';
import { PanierService } from '../service/panier.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

@Component({
  selector: 'jhi-panier-update',
  templateUrl: './panier-update.component.html',
})
export class PanierUpdateComponent implements OnInit {
  isSaving = false;

  utilisateursCollection: IUtilisateur[] = [];

  editForm = this.fb.group({
    id: [],
    utilisateur: [],
  });

  constructor(
    protected panierService: PanierService,
    protected utilisateurService: UtilisateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ panier }) => {
      this.updateForm(panier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const panier = this.createFromForm();
    if (panier.id !== undefined) {
      this.subscribeToSaveResponse(this.panierService.update(panier));
    } else {
      this.subscribeToSaveResponse(this.panierService.create(panier));
    }
  }

  trackUtilisateurById(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPanier>>): void {
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

  protected updateForm(panier: IPanier): void {
    this.editForm.patchValue({
      id: panier.id,
      utilisateur: panier.utilisateur,
    });

    this.utilisateursCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursCollection,
      panier.utilisateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query({ filter: 'panier-is-null' })
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.editForm.get('utilisateur')!.value)
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursCollection = utilisateurs));
  }

  protected createFromForm(): IPanier {
    return {
      ...new Panier(),
      id: this.editForm.get(['id'])!.value,
      utilisateur: this.editForm.get(['utilisateur'])!.value,
    };
  }
}
