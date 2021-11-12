import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISouhait, Souhait } from '../souhait.model';

import { SouhaitService } from './souhait.service';

describe('Souhait Service', () => {
  let service: SouhaitService;
  let httpMock: HttpTestingController;
  let elemDefault: ISouhait;
  let expectedResult: ISouhait | ISouhait[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SouhaitService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Souhait', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Souhait()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Souhait', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Souhait', () => {
      const patchObject = Object.assign({}, new Souhait());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Souhait', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Souhait', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSouhaitToCollectionIfMissing', () => {
      it('should add a Souhait to an empty array', () => {
        const souhait: ISouhait = { id: 123 };
        expectedResult = service.addSouhaitToCollectionIfMissing([], souhait);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(souhait);
      });

      it('should not add a Souhait to an array that contains it', () => {
        const souhait: ISouhait = { id: 123 };
        const souhaitCollection: ISouhait[] = [
          {
            ...souhait,
          },
          { id: 456 },
        ];
        expectedResult = service.addSouhaitToCollectionIfMissing(souhaitCollection, souhait);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Souhait to an array that doesn't contain it", () => {
        const souhait: ISouhait = { id: 123 };
        const souhaitCollection: ISouhait[] = [{ id: 456 }];
        expectedResult = service.addSouhaitToCollectionIfMissing(souhaitCollection, souhait);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(souhait);
      });

      it('should add only unique Souhait to an array', () => {
        const souhaitArray: ISouhait[] = [{ id: 123 }, { id: 456 }, { id: 85174 }];
        const souhaitCollection: ISouhait[] = [{ id: 123 }];
        expectedResult = service.addSouhaitToCollectionIfMissing(souhaitCollection, ...souhaitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const souhait: ISouhait = { id: 123 };
        const souhait2: ISouhait = { id: 456 };
        expectedResult = service.addSouhaitToCollectionIfMissing([], souhait, souhait2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(souhait);
        expect(expectedResult).toContain(souhait2);
      });

      it('should accept null and undefined values', () => {
        const souhait: ISouhait = { id: 123 };
        expectedResult = service.addSouhaitToCollectionIfMissing([], null, souhait, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(souhait);
      });

      it('should return initial array if no Souhait is added', () => {
        const souhaitCollection: ISouhait[] = [{ id: 123 }];
        expectedResult = service.addSouhaitToCollectionIfMissing(souhaitCollection, undefined, null);
        expect(expectedResult).toEqual(souhaitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
