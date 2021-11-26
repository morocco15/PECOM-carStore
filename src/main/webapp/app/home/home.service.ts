import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';

@Injectable({ providedIn: 'root' })
export class HomeService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/voiture');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getQuatreDernieresVoitures(debut: number, fin: number): Observable<IVoiture[]> {
    //eslint-disable-next-line no-console
    console.error(this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`));
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`);
  }
}
