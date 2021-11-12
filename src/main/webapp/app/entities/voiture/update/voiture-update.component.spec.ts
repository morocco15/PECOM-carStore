jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VoitureService } from '../service/voiture.service';
import { IVoiture, Voiture } from '../voiture.model';
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

import { VoitureUpdateComponent } from './voiture-update.component';

describe('Voiture Management Update Component', () => {
  let comp: VoitureUpdateComponent;
  let fixture: ComponentFixture<VoitureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let voitureService: VoitureService;
  let marqueService: MarqueService;
  let categorieService: CategorieService;
  let commandeService: CommandeService;
  let panierService: PanierService;
  let vendeurService: VendeurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VoitureUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(VoitureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VoitureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    voitureService = TestBed.inject(VoitureService);
    marqueService = TestBed.inject(MarqueService);
    categorieService = TestBed.inject(CategorieService);
    commandeService = TestBed.inject(CommandeService);
    panierService = TestBed.inject(PanierService);
    vendeurService = TestBed.inject(VendeurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Marque query and add missing value', () => {
      const voiture: IVoiture = { id: 456 };
      const marque: IMarque = { id: 80991 };
      voiture.marque = marque;

      const marqueCollection: IMarque[] = [{ id: 26511 }];
      jest.spyOn(marqueService, 'query').mockReturnValue(of(new HttpResponse({ body: marqueCollection })));
      const additionalMarques = [marque];
      const expectedCollection: IMarque[] = [...additionalMarques, ...marqueCollection];
      jest.spyOn(marqueService, 'addMarqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      expect(marqueService.query).toHaveBeenCalled();
      expect(marqueService.addMarqueToCollectionIfMissing).toHaveBeenCalledWith(marqueCollection, ...additionalMarques);
      expect(comp.marquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Categorie query and add missing value', () => {
      const voiture: IVoiture = { id: 456 };
      const categories: ICategorie[] = [{ id: 61044 }];
      voiture.categories = categories;

      const categorieCollection: ICategorie[] = [{ id: 64545 }];
      jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
      const additionalCategories = [...categories];
      const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
      jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      expect(categorieService.query).toHaveBeenCalled();
      expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Commande query and add missing value', () => {
      const voiture: IVoiture = { id: 456 };
      const commande: ICommande = { id: 67687 };
      voiture.commande = commande;

      const commandeCollection: ICommande[] = [{ id: 83475 }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [commande];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Panier query and add missing value', () => {
      const voiture: IVoiture = { id: 456 };
      const panier: IPanier = { id: 91013 };
      voiture.panier = panier;

      const panierCollection: IPanier[] = [{ id: 68808 }];
      jest.spyOn(panierService, 'query').mockReturnValue(of(new HttpResponse({ body: panierCollection })));
      const additionalPaniers = [panier];
      const expectedCollection: IPanier[] = [...additionalPaniers, ...panierCollection];
      jest.spyOn(panierService, 'addPanierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      expect(panierService.query).toHaveBeenCalled();
      expect(panierService.addPanierToCollectionIfMissing).toHaveBeenCalledWith(panierCollection, ...additionalPaniers);
      expect(comp.paniersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vendeur query and add missing value', () => {
      const voiture: IVoiture = { id: 456 };
      const vendeur: IVendeur = { id: 94441 };
      voiture.vendeur = vendeur;

      const vendeurCollection: IVendeur[] = [{ id: 94387 }];
      jest.spyOn(vendeurService, 'query').mockReturnValue(of(new HttpResponse({ body: vendeurCollection })));
      const additionalVendeurs = [vendeur];
      const expectedCollection: IVendeur[] = [...additionalVendeurs, ...vendeurCollection];
      jest.spyOn(vendeurService, 'addVendeurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      expect(vendeurService.query).toHaveBeenCalled();
      expect(vendeurService.addVendeurToCollectionIfMissing).toHaveBeenCalledWith(vendeurCollection, ...additionalVendeurs);
      expect(comp.vendeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const voiture: IVoiture = { id: 456 };
      const marque: IMarque = { id: 2011 };
      voiture.marque = marque;
      const categories: ICategorie = { id: 81646 };
      voiture.categories = [categories];
      const commande: ICommande = { id: 18067 };
      voiture.commande = commande;
      const panier: IPanier = { id: 96544 };
      voiture.panier = panier;
      const vendeur: IVendeur = { id: 44135 };
      voiture.vendeur = vendeur;

      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(voiture));
      expect(comp.marquesSharedCollection).toContain(marque);
      expect(comp.categoriesSharedCollection).toContain(categories);
      expect(comp.commandesSharedCollection).toContain(commande);
      expect(comp.paniersSharedCollection).toContain(panier);
      expect(comp.vendeursSharedCollection).toContain(vendeur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Voiture>>();
      const voiture = { id: 123 };
      jest.spyOn(voitureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: voiture }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(voitureService.update).toHaveBeenCalledWith(voiture);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Voiture>>();
      const voiture = new Voiture();
      jest.spyOn(voitureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: voiture }));
      saveSubject.complete();

      // THEN
      expect(voitureService.create).toHaveBeenCalledWith(voiture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Voiture>>();
      const voiture = { id: 123 };
      jest.spyOn(voitureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voiture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(voitureService.update).toHaveBeenCalledWith(voiture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMarqueById', () => {
      it('Should return tracked Marque primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMarqueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCategorieById', () => {
      it('Should return tracked Categorie primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategorieById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCommandeById', () => {
      it('Should return tracked Commande primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCommandeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPanierById', () => {
      it('Should return tracked Panier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPanierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVendeurById', () => {
      it('Should return tracked Vendeur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVendeurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCategorie', () => {
      it('Should return option if no Categorie is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCategorie(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Categorie for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCategorie(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Categorie is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCategorie(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
