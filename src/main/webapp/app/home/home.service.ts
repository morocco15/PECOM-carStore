import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class HomeService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/voiture');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getQuatreDernieresVoitures(debut: number, fin: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`);
  }

  public ajouterVoiturePanier(username: string, id:number,version:number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${username}/${id}/${version}`);
  }
}
