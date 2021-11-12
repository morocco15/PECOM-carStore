import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISouhait, Souhait } from '../souhait.model';
import { SouhaitService } from '../service/souhait.service';

@Injectable({ providedIn: 'root' })
export class SouhaitRoutingResolveService implements Resolve<ISouhait> {
  constructor(protected service: SouhaitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISouhait> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((souhait: HttpResponse<Souhait>) => {
          if (souhait.body) {
            return of(souhait.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Souhait());
  }
}
