import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';

@Injectable({ providedIn: 'root' })
export class SouhaitService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/souhait');
  private resourceUrl2 = this.applicationConfigService.getEndpointFor('api/souhait_sup');
  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public ajouterVoitureSouhait(username: string, id:number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${username}/${id}`);
  }

  public getSouhait(username: string):Observable<IVoiture[]>{
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${username}`);
  }

  public supprimerVoitureSouhait(username: string, id:number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl2}/${username}/${id}`);
  }

}
