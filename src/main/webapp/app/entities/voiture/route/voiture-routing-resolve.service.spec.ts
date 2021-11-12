jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVoiture, Voiture } from '../voiture.model';
import { VoitureService } from '../service/voiture.service';

import { VoitureRoutingResolveService } from './voiture-routing-resolve.service';

describe('Voiture routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VoitureRoutingResolveService;
  let service: VoitureService;
  let resultVoiture: IVoiture | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VoitureRoutingResolveService);
    service = TestBed.inject(VoitureService);
    resultVoiture = undefined;
  });

  describe('resolve', () => {
    it('should return IVoiture returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVoiture = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVoiture).toEqual({ id: 123 });
    });

    it('should return new IVoiture if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVoiture = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVoiture).toEqual(new Voiture());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Voiture })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVoiture = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVoiture).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
