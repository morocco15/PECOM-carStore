import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVoiture, getVoitureIdentifier } from '../voiture.model';

export type EntityResponseType = HttpResponse<IVoiture>;
export type EntityArrayResponseType = HttpResponse<IVoiture[]>;

@Injectable({ providedIn: 'root' })
export class VoitureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/voitures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(voiture: IVoiture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voiture);
    return this.http
      .post<IVoiture>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(voiture: IVoiture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voiture);
    return this.http
      .put<IVoiture>(`${this.resourceUrl}/${getVoitureIdentifier(voiture) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(voiture: IVoiture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voiture);
    return this.http
      .patch<IVoiture>(`${this.resourceUrl}/${getVoitureIdentifier(voiture) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVoiture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVoiture[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVoitureToCollectionIfMissing(voitureCollection: IVoiture[], ...voituresToCheck: (IVoiture | null | undefined)[]): IVoiture[] {
    const voitures: IVoiture[] = voituresToCheck.filter(isPresent);
    if (voitures.length > 0) {
      const voitureCollectionIdentifiers = voitureCollection.map(voitureItem => getVoitureIdentifier(voitureItem)!);
      const voituresToAdd = voitures.filter(voitureItem => {
        const voitureIdentifier = getVoitureIdentifier(voitureItem);
        if (voitureIdentifier == null || voitureCollectionIdentifiers.includes(voitureIdentifier)) {
          return false;
        }
        voitureCollectionIdentifiers.push(voitureIdentifier);
        return true;
      });
      return [...voituresToAdd, ...voitureCollection];
    }
    return voitureCollection;
  }

  protected convertDateFromClient(voiture: IVoiture): IVoiture {
    return Object.assign({}, voiture, {
      miseEnVente: voiture.miseEnVente?.isValid() ? voiture.miseEnVente.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.miseEnVente = res.body.miseEnVente ? dayjs(res.body.miseEnVente) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((voiture: IVoiture) => {
        voiture.miseEnVente = voiture.miseEnVente ? dayjs(voiture.miseEnVente) : undefined;
      });
    }
    return res;
  }
}
