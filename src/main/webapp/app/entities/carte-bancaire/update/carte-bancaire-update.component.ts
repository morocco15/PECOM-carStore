import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';

@Component({
  selector: 'jhi-carte-bancaire-update',
  templateUrl: './carte-bancaire-update.component.html',
})
export class CarteBancaireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, []],
    code: [],
    expiration: [],
    prenom: [],
    nom: [],
  });

  constructor(protected carteBancaireService: CarteBancaireService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteBancaire }) => {
      if (carteBancaire.id === undefined) {
        const today = dayjs().startOf('day');
        carteBancaire.expiration = today;
      }

      this.updateForm(carteBancaire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carteBancaire = this.createFromForm();
    if (carteBancaire.id !== undefined) {
      this.subscribeToSaveResponse(this.carteBancaireService.update(carteBancaire));
    } else {
      this.subscribeToSaveResponse(this.carteBancaireService.create(carteBancaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarteBancaire>>): void {
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

  protected updateForm(carteBancaire: ICarteBancaire): void {
    this.editForm.patchValue({
      id: carteBancaire.id,
      code: carteBancaire.code,
      expiration: carteBancaire.expiration ? carteBancaire.expiration.format(DATE_TIME_FORMAT) : null,
      prenom: carteBancaire.prenom,
      nom: carteBancaire.nom,
    });
  }

  protected createFromForm(): ICarteBancaire {
    return {
      ...new CarteBancaire(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      expiration: this.editForm.get(['expiration'])!.value ? dayjs(this.editForm.get(['expiration'])!.value, DATE_TIME_FORMAT) : undefined,
      prenom: this.editForm.get(['prenom'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
