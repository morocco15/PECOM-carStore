import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IVoiture } from '../entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';

@Injectable({ providedIn: 'root' })
export class PanierService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/voiture');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getQuatreDernieresVoitures(debut: number, fin: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`);
  }
}
