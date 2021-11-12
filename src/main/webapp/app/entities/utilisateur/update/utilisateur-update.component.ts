import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUtilisateur, Utilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICarteBancaire } from 'app/entities/carte-bancaire/carte-bancaire.model';
import { CarteBancaireService } from 'app/entities/carte-bancaire/service/carte-bancaire.service';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  idPaimentsCollection: ICarteBancaire[] = [];

  editForm = this.fb.group({
    id: [],
    idcompte: [],
    idPaiment: [],
  });

  constructor(
    protected utilisateurService: UtilisateurService,
    protected userService: UserService,
    protected carteBancaireService: CarteBancaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      this.updateForm(utilisateur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateur = this.createFromForm();
    if (utilisateur.id !== undefined) {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    } else {
      this.subscribeToSaveResponse(this.utilisateurService.create(utilisateur));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCarteBancaireById(index: number, item: ICarteBancaire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateur>>): void {
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

  protected updateForm(utilisateur: IUtilisateur): void {
    this.editForm.patchValue({
      id: utilisateur.id,
      idcompte: utilisateur.idcompte,
      idPaiment: utilisateur.idPaiment,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, utilisateur.idcompte);
    this.idPaimentsCollection = this.carteBancaireService.addCarteBancaireToCollectionIfMissing(
      this.idPaimentsCollection,
      utilisateur.idPaiment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('idcompte')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.carteBancaireService
      .query({ filter: 'utilisateur-is-null' })
      .pipe(map((res: HttpResponse<ICarteBancaire[]>) => res.body ?? []))
      .pipe(
        map((carteBancaires: ICarteBancaire[]) =>
          this.carteBancaireService.addCarteBancaireToCollectionIfMissing(carteBancaires, this.editForm.get('idPaiment')!.value)
        )
      )
      .subscribe((carteBancaires: ICarteBancaire[]) => (this.idPaimentsCollection = carteBancaires));
  }

  protected createFromForm(): IUtilisateur {
    return {
      ...new Utilisateur(),
      id: this.editForm.get(['id'])!.value,
      idcompte: this.editForm.get(['idcompte'])!.value,
      idPaiment: this.editForm.get(['idPaiment'])!.value,
    };
  }
}
