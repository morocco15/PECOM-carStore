jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SouhaitService } from '../service/souhait.service';
import { ISouhait, Souhait } from '../souhait.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { VoitureService } from 'app/entities/voiture/service/voiture.service';

import { SouhaitUpdateComponent } from './souhait-update.component';

describe('Souhait Management Update Component', () => {
  let comp: SouhaitUpdateComponent;
  let fixture: ComponentFixture<SouhaitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let souhaitService: SouhaitService;
  let utilisateurService: UtilisateurService;
  let voitureService: VoitureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SouhaitUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SouhaitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SouhaitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    souhaitService = TestBed.inject(SouhaitService);
    utilisateurService = TestBed.inject(UtilisateurService);
    voitureService = TestBed.inject(VoitureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call utilisateur query and add missing value', () => {
      const souhait: ISouhait = { id: 456 };
      const utilisateur: IUtilisateur = { id: 60655 };
      souhait.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 49167 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const expectedCollection: IUtilisateur[] = [utilisateur, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ souhait });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(utilisateurCollection, utilisateur);
      expect(comp.utilisateursCollection).toEqual(expectedCollection);
    });

    it('Should call Voiture query and add missing value', () => {
      const souhait: ISouhait = { id: 456 };
      const voitures: IVoiture[] = [{ id: 56065 }];
      souhait.voitures = voitures;

      const voitureCollection: IVoiture[] = [{ id: 64404 }];
      jest.spyOn(voitureService, 'query').mockReturnValue(of(new HttpResponse({ body: voitureCollection })));
      const additionalVoitures = [...voitures];
      const expectedCollection: IVoiture[] = [...additionalVoitures, ...voitureCollection];
      jest.spyOn(voitureService, 'addVoitureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ souhait });
      comp.ngOnInit();

      expect(voitureService.query).toHaveBeenCalled();
      expect(voitureService.addVoitureToCollectionIfMissing).toHaveBeenCalledWith(voitureCollection, ...additionalVoitures);
      expect(comp.voituresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const souhait: ISouhait = { id: 456 };
      const utilisateur: IUtilisateur = { id: 78998 };
      souhait.utilisateur = utilisateur;
      const voitures: IVoiture = { id: 19504 };
      souhait.voitures = [voitures];

      activatedRoute.data = of({ souhait });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(souhait));
      expect(comp.utilisateursCollection).toContain(utilisateur);
      expect(comp.voituresSharedCollection).toContain(voitures);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Souhait>>();
      const souhait = { id: 123 };
      jest.spyOn(souhaitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ souhait });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: souhait }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(souhaitService.update).toHaveBeenCalledWith(souhait);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Souhait>>();
      const souhait = new Souhait();
      jest.spyOn(souhaitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ souhait });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: souhait }));
      saveSubject.complete();

      // THEN
      expect(souhaitService.create).toHaveBeenCalledWith(souhait);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Souhait>>();
      const souhait = { id: 123 };
      jest.spyOn(souhaitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ souhait });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(souhaitService.update).toHaveBeenCalledWith(souhait);
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

    describe('trackVoitureById', () => {
      it('Should return tracked Voiture primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVoitureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedVoiture', () => {
      it('Should return option if no Voiture is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedVoiture(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Voiture for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedVoiture(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Voiture is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedVoiture(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
