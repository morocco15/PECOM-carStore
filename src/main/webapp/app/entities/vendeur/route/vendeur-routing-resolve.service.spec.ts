jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVendeur, Vendeur } from '../vendeur.model';
import { VendeurService } from '../service/vendeur.service';

import { VendeurRoutingResolveService } from './vendeur-routing-resolve.service';

describe('Vendeur routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VendeurRoutingResolveService;
  let service: VendeurService;
  let resultVendeur: IVendeur | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VendeurRoutingResolveService);
    service = TestBed.inject(VendeurService);
    resultVendeur = undefined;
  });

  describe('resolve', () => {
    it('should return IVendeur returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVendeur = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVendeur).toEqual({ id: 123 });
    });

    it('should return new IVendeur if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVendeur = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVendeur).toEqual(new Vendeur());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vendeur })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVendeur = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVendeur).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
