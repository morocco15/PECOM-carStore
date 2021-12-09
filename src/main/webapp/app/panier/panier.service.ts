import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';

@Injectable({ providedIn: 'root' })
export class PanierService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/panier');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getQuatreDernieresVoitures(debut: number, fin: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`);
  }

  public ajouterVoiturePanier(username: string, id:number,version:number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${username}/${id}/${version}`);
  }

}
