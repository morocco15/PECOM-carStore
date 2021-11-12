import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVendeur, Vendeur } from '../vendeur.model';
import { VendeurService } from '../service/vendeur.service';

@Component({
  selector: 'jhi-vendeur-update',
  templateUrl: './vendeur-update.component.html',
})
export class VendeurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, []],
    login: [],
    coordonnee: [],
  });

  constructor(protected vendeurService: VendeurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendeur }) => {
      this.updateForm(vendeur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendeur = this.createFromForm();
    if (vendeur.id !== undefined) {
      this.subscribeToSaveResponse(this.vendeurService.update(vendeur));
    } else {
      this.subscribeToSaveResponse(this.vendeurService.create(vendeur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendeur>>): void {
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

  protected updateForm(vendeur: IVendeur): void {
    this.editForm.patchValue({
      id: vendeur.id,
      login: vendeur.login,
      coordonnee: vendeur.coordonnee,
    });
  }

  protected createFromForm(): IVendeur {
    return {
      ...new Vendeur(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      coordonnee: this.editForm.get(['coordonnee'])!.value,
    };
  }
}
