import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Statut } from 'app/entities/enumerations/statut.model';
import { Etat } from 'app/entities/enumerations/etat.model';
import { Carburant } from 'app/entities/enumerations/carburant.model';
import { IVoiture, Voiture } from '../voiture.model';

import { VoitureService } from './voiture.service';

describe('Voiture Service', () => {
  let service: VoitureService;
  let httpMock: HttpTestingController;
  let elemDefault: IVoiture;
  let expectedResult: IVoiture | IVoiture[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VoitureService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      model: 'AAAAAAA',
      prix: 0,
      image1: 'AAAAAAA',
      image2: 'AAAAAAA',
      image3: 'AAAAAAA',
      statut: Statut.RESERVER,
      version: 0,
      miseEnVente: currentDate,
      etat: Etat.NONROULANT,
      porte: 0,
      boiteVitesse: 0,
      co2: 0,
      chevaux: 0,
      carburant: Carburant.ESSENCE,
      annees: currentDate,
      ville: 'AAAAAAA',
      codePostal: 0,
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          miseEnVente: currentDate.format(DATE_TIME_FORMAT),
          annees: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Voiture', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          miseEnVente: currentDate.format(DATE_TIME_FORMAT),
          annees: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          miseEnVente: currentDate,
          annees: currentDate,
        },
        returnedFromService
      );

      service.create(new Voiture()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Voiture', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          model: 'BBBBBB',
          prix: 1,
          image1: 'BBBBBB',
          image2: 'BBBBBB',
          image3: 'BBBBBB',
          statut: 'BBBBBB',
          version: 1,
          miseEnVente: currentDate.format(DATE_TIME_FORMAT),
          etat: 'BBBBBB',
          porte: 1,
          boiteVitesse: 1,
          co2: 1,
          chevaux: 1,
          carburant: 'BBBBBB',
          annees: currentDate.format(DATE_TIME_FORMAT),
          ville: 'BBBBBB',
          codePostal: 1,
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          miseEnVente: currentDate,
          annees: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Voiture', () => {
      const patchObject = Object.assign(
        {
          prix: 1,
          statut: 'BBBBBB',
          miseEnVente: currentDate.format(DATE_TIME_FORMAT),
          etat: 'BBBBBB',
          porte: 1,
          co2: 1,
          chevaux: 1,
          codePostal: 1,
          description: 'BBBBBB',
        },
        new Voiture()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          miseEnVente: currentDate,
          annees: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Voiture', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          model: 'BBBBBB',
          prix: 1,
          image1: 'BBBBBB',
          image2: 'BBBBBB',
          image3: 'BBBBBB',
          statut: 'BBBBBB',
          version: 1,
          miseEnVente: currentDate.format(DATE_TIME_FORMAT),
          etat: 'BBBBBB',
          porte: 1,
          boiteVitesse: 1,
          co2: 1,
          chevaux: 1,
          carburant: 'BBBBBB',
          annees: currentDate.format(DATE_TIME_FORMAT),
          ville: 'BBBBBB',
          codePostal: 1,
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          miseEnVente: currentDate,
          annees: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Voiture', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVoitureToCollectionIfMissing', () => {
      it('should add a Voiture to an empty array', () => {
        const voiture: IVoiture = { id: 123 };
        expectedResult = service.addVoitureToCollectionIfMissing([], voiture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(voiture);
      });

      it('should not add a Voiture to an array that contains it', () => {
        const voiture: IVoiture = { id: 123 };
        const voitureCollection: IVoiture[] = [
          {
            ...voiture,
          },
          { id: 456 },
        ];
        expectedResult = service.addVoitureToCollectionIfMissing(voitureCollection, voiture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Voiture to an array that doesn't contain it", () => {
        const voiture: IVoiture = { id: 123 };
        const voitureCollection: IVoiture[] = [{ id: 456 }];
        expectedResult = service.addVoitureToCollectionIfMissing(voitureCollection, voiture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(voiture);
      });

      it('should add only unique Voiture to an array', () => {
        const voitureArray: IVoiture[] = [{ id: 123 }, { id: 456 }, { id: 97568 }];
        const voitureCollection: IVoiture[] = [{ id: 123 }];
        expectedResult = service.addVoitureToCollectionIfMissing(voitureCollection, ...voitureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const voiture: IVoiture = { id: 123 };
        const voiture2: IVoiture = { id: 456 };
        expectedResult = service.addVoitureToCollectionIfMissing([], voiture, voiture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(voiture);
        expect(expectedResult).toContain(voiture2);
      });

      it('should accept null and undefined values', () => {
        const voiture: IVoiture = { id: 123 };
        expectedResult = service.addVoitureToCollectionIfMissing([], null, voiture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(voiture);
      });

      it('should return initial array if no Voiture is added', () => {
        const voitureCollection: IVoiture[] = [{ id: 123 }];
        expectedResult = service.addVoitureToCollectionIfMissing(voitureCollection, undefined, null);
        expect(expectedResult).toEqual(voitureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
