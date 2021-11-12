import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISouhait, getSouhaitIdentifier } from '../souhait.model';

export type EntityResponseType = HttpResponse<ISouhait>;
export type EntityArrayResponseType = HttpResponse<ISouhait[]>;

@Injectable({ providedIn: 'root' })
export class SouhaitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/souhaits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(souhait: ISouhait): Observable<EntityResponseType> {
    return this.http.post<ISouhait>(this.resourceUrl, souhait, { observe: 'response' });
  }

  update(souhait: ISouhait): Observable<EntityResponseType> {
    return this.http.put<ISouhait>(`${this.resourceUrl}/${getSouhaitIdentifier(souhait) as number}`, souhait, { observe: 'response' });
  }

  partialUpdate(souhait: ISouhait): Observable<EntityResponseType> {
    return this.http.patch<ISouhait>(`${this.resourceUrl}/${getSouhaitIdentifier(souhait) as number}`, souhait, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISouhait>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISouhait[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSouhaitToCollectionIfMissing(souhaitCollection: ISouhait[], ...souhaitsToCheck: (ISouhait | null | undefined)[]): ISouhait[] {
    const souhaits: ISouhait[] = souhaitsToCheck.filter(isPresent);
    if (souhaits.length > 0) {
      const souhaitCollectionIdentifiers = souhaitCollection.map(souhaitItem => getSouhaitIdentifier(souhaitItem)!);
      const souhaitsToAdd = souhaits.filter(souhaitItem => {
        const souhaitIdentifier = getSouhaitIdentifier(souhaitItem);
        if (souhaitIdentifier == null || souhaitCollectionIdentifiers.includes(souhaitIdentifier)) {
          return false;
        }
        souhaitCollectionIdentifiers.push(souhaitIdentifier);
        return true;
      });
      return [...souhaitsToAdd, ...souhaitCollection];
    }
    return souhaitCollection;
  }
}
