import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ArticleService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/article');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
/*
  public getVoitureId(id: number,): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${id}/`);
  }
 */

}
