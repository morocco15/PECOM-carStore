import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVoiture, Voiture } from '../voiture.model';
import { VoitureService } from '../service/voiture.service';

@Injectable({ providedIn: 'root' })
export class VoitureRoutingResolveService implements Resolve<IVoiture> {
  constructor(protected service: VoitureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVoiture> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((voiture: HttpResponse<Voiture>) => {
          if (voiture.body) {
            return of(voiture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Voiture());
  }
}
