import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';

@Injectable({ providedIn: 'root' })
export class SouhaitService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/souhait');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public ajouterVoitureSouhait(username: string, id:number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${username}/${id}`);
  }

}
