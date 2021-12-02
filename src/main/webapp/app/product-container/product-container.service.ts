import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';
@Injectable({
  providedIn: 'root'
})
export class ProductContainerService
{

  private resourceUrl = this.applicationConfigService.getEndpointFor('api/voiture');


  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getVoitures(id: number): Observable<IVoiture> {
    // return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`);
    return this.httpClient.get<IVoiture>(`${this.resourceUrl}/${id}`);
  }


  public getVoitureImageURL(id:number):Observable<string>{
    return this.httpClient.get<string>(`${this.resourceUrl}/${id}`)
  }
}
