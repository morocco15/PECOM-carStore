import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVoiture, Voiture } from '../voiture.model';
import { VoitureService } from '../service/voiture.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMarque } from 'app/entities/marque/marque.model';
import { MarqueService } from 'app/entities/marque/service/marque.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { IPanier } from 'app/entities/panier/panier.model';
import { PanierService } from 'app/entities/panier/service/panier.service';
import { IVendeur } from 'app/entities/vendeur/vendeur.model';
import { VendeurService } from 'app/entities/vendeur/service/vendeur.service';
import { Statut } from 'app/entities/enumerations/statut.model';
import { Etat } from 'app/entities/enumerations/etat.model';
import { Carburant } from 'app/entities/enumerations/carburant.model';

@Component({
  selector: 'jhi-voiture-update',
  templateUrl: './voiture-update.component.html',
})
export class VoitureUpdateComponent implements OnInit {
  isSaving = false;
  statutValues = Object.keys(Statut);
  etatValues = Object.keys(Etat);
  carburantValues = Object.keys(Carburant);

  marquesSharedCollection: IMarque[] = [];
  categoriesSharedCollection: ICategorie[] = [];
  commandesSharedCollection: ICommande[] = [];
  paniersSharedCollection: IPanier[] = [];
  vendeursSharedCollection: IVendeur[] = [];

  editForm = this.fb.group({
    id: [null, []],
    model: [],
    prix: [],
    image1: [],
    image1ContentType: [],
    image2: [],
    image2ContentType: [],
    image3: [],
    image3ContentType: [],
    statut: [],
    miseEnVente: [],
    etat: [],
    porte: [null, [Validators.max(5)]],
    boiteVitesse: [],
    co2: [],
    chevaux: [],
    carburant: [],
    marque: [],
    categories: [],
    commande: [],
    panier: [],
    vendeur: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected voitureService: VoitureService,
    protected marqueService: MarqueService,
    protected categorieService: CategorieService,
    protected commandeService: CommandeService,
    protected panierService: PanierService,
    protected vendeurService: VendeurService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voiture }) => {
      if (voiture.id === undefined) {
        const today = dayjs().startOf('day');
        voiture.miseEnVente = today;
      }

      this.updateForm(voiture);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('carStoreApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const voiture = this.createFromForm();
    if (voiture.id !== undefined) {
      this.subscribeToSaveResponse(this.voitureService.update(voiture));
    } else {
      this.subscribeToSaveResponse(this.voitureService.create(voiture));
    }
  }

  trackMarqueById(index: number, item: IMarque): number {
    return item.id!;
  }

  trackCategorieById(index: number, item: ICategorie): number {
    return item.id!;
  }

  trackCommandeById(index: number, item: ICommande): number {
    return item.id!;
  }

  trackPanierById(index: number, item: IPanier): number {
    return item.id!;
  }

  trackVendeurById(index: number, item: IVendeur): number {
    return item.id!;
  }

  getSelectedCategorie(option: ICategorie, selectedVals?: ICategorie[]): ICategorie {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoiture>>): void {
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

  protected updateForm(voiture: IVoiture): void {
    this.editForm.patchValue({
      id: voiture.id,
      model: voiture.model,
      prix: voiture.prix,
      image1: voiture.image1,
      image1ContentType: voiture.image1ContentType,
      image2: voiture.image2,
      image2ContentType: voiture.image2ContentType,
      image3: voiture.image3,
      image3ContentType: voiture.image3ContentType,
      statut: voiture.statut,
      miseEnVente: voiture.miseEnVente ? voiture.miseEnVente.format(DATE_TIME_FORMAT) : null,
      etat: voiture.etat,
      porte: voiture.porte,
      boiteVitesse: voiture.boiteVitesse,
      co2: voiture.co2,
      chevaux: voiture.chevaux,
      carburant: voiture.carburant,
      marque: voiture.marque,
      categories: voiture.categories,
      commande: voiture.commande,
      panier: voiture.panier,
      vendeur: voiture.vendeur,
    });

    this.marquesSharedCollection = this.marqueService.addMarqueToCollectionIfMissing(this.marquesSharedCollection, voiture.marque);
    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      ...(voiture.categories ?? [])
    );
    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      voiture.commande
    );
    this.paniersSharedCollection = this.panierService.addPanierToCollectionIfMissing(this.paniersSharedCollection, voiture.panier);
    this.vendeursSharedCollection = this.vendeurService.addVendeurToCollectionIfMissing(this.vendeursSharedCollection, voiture.vendeur);
  }

  protected loadRelationshipsOptions(): void {
    this.marqueService
      .query()
      .pipe(map((res: HttpResponse<IMarque[]>) => res.body ?? []))
      .pipe(map((marques: IMarque[]) => this.marqueService.addMarqueToCollectionIfMissing(marques, this.editForm.get('marque')!.value)))
      .subscribe((marques: IMarque[]) => (this.marquesSharedCollection = marques));

    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, ...(this.editForm.get('categories')!.value ?? []))
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, this.editForm.get('commande')!.value)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));

    this.panierService
      .query()
      .pipe(map((res: HttpResponse<IPanier[]>) => res.body ?? []))
      .pipe(map((paniers: IPanier[]) => this.panierService.addPanierToCollectionIfMissing(paniers, this.editForm.get('panier')!.value)))
      .subscribe((paniers: IPanier[]) => (this.paniersSharedCollection = paniers));

    this.vendeurService
      .query()
      .pipe(map((res: HttpResponse<IVendeur[]>) => res.body ?? []))
      .pipe(
        map((vendeurs: IVendeur[]) => this.vendeurService.addVendeurToCollectionIfMissing(vendeurs, this.editForm.get('vendeur')!.value))
      )
      .subscribe((vendeurs: IVendeur[]) => (this.vendeursSharedCollection = vendeurs));
  }

  protected createFromForm(): IVoiture {
    return {
      ...new Voiture(),
      id: this.editForm.get(['id'])!.value,
      model: this.editForm.get(['model'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      image1ContentType: this.editForm.get(['image1ContentType'])!.value,
      image1: this.editForm.get(['image1'])!.value,
      image2ContentType: this.editForm.get(['image2ContentType'])!.value,
      image2: this.editForm.get(['image2'])!.value,
      image3ContentType: this.editForm.get(['image3ContentType'])!.value,
      image3: this.editForm.get(['image3'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      miseEnVente: this.editForm.get(['miseEnVente'])!.value
        ? dayjs(this.editForm.get(['miseEnVente'])!.value, DATE_TIME_FORMAT)
        : undefined,
      etat: this.editForm.get(['etat'])!.value,
      porte: this.editForm.get(['porte'])!.value,
      boiteVitesse: this.editForm.get(['boiteVitesse'])!.value,
      co2: this.editForm.get(['co2'])!.value,
      chevaux: this.editForm.get(['chevaux'])!.value,
      carburant: this.editForm.get(['carburant'])!.value,
      marque: this.editForm.get(['marque'])!.value,
      categories: this.editForm.get(['categories'])!.value,
      commande: this.editForm.get(['commande'])!.value,
      panier: this.editForm.get(['panier'])!.value,
      vendeur: this.editForm.get(['vendeur'])!.value,
    };
  }
}
