jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PanierService } from '../service/panier.service';
import { IPanier, Panier } from '../panier.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { PanierUpdateComponent } from './panier-update.component';

describe('Panier Management Update Component', () => {
  let comp: PanierUpdateComponent;
  let fixture: ComponentFixture<PanierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let panierService: PanierService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PanierUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PanierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PanierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    panierService = TestBed.inject(PanierService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call utilisateur query and add missing value', () => {
      const panier: IPanier = { id: 456 };
      const utilisateur: IUtilisateur = { id: 74442 };
      panier.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 97642 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const expectedCollection: IUtilisateur[] = [utilisateur, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ panier });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(utilisateurCollection, utilisateur);
      expect(comp.utilisateursCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const panier: IPanier = { id: 456 };
      const utilisateur: IUtilisateur = { id: 12336 };
      panier.utilisateur = utilisateur;

      activatedRoute.data = of({ panier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(panier));
      expect(comp.utilisateursCollection).toContain(utilisateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Panier>>();
      const panier = { id: 123 };
      jest.spyOn(panierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ panier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: panier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(panierService.update).toHaveBeenCalledWith(panier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Panier>>();
      const panier = new Panier();
      jest.spyOn(panierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ panier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: panier }));
      saveSubject.complete();

      // THEN
      expect(panierService.create).toHaveBeenCalledWith(panier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Panier>>();
      const panier = { id: 123 };
      jest.spyOn(panierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ panier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(panierService.update).toHaveBeenCalledWith(panier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUtilisateurById', () => {
      it('Should return tracked Utilisateur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUtilisateurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
