import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IVoiture, Voiture } from '../entities/voiture/voiture.model';
import { Observable } from 'rxjs';
import { Etat } from 'app/entities/enumerations/etat.model';

@Injectable({ providedIn: 'root' })
export class HomeService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/voiture');
  private resourceUrlCategorie = this.applicationConfigService.getEndpointFor('api/listCategorie');
  private resourceUrlVoitureCategorie = this.applicationConfigService.getEndpointFor('api/limiteCategorie');
  private resourceUrlS = this.applicationConfigService.getEndpointForS('api/voitures');
  private resourceUrlPMax = this.applicationConfigService.getEndpointForS('api/maxPrix');
  private resourceUrlPMin = this.applicationConfigService.getEndpointForS('api/minPrix');
  private resourceUrlPInter = this.applicationConfigService.getEndpointForS('api/limitePrix');
  private resourceUrlEtat = this.applicationConfigService.getEndpointForS('api/limiteEtat');

  constructor(private httpClient: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getVoituresRecentes(debut: number, fin: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrl}/${debut}/${fin}`);
  }

  public ajouterVoiturePanier(username: string, id: number, version: number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${username}/${id}/${version}`);
  }

  public getVoituresByID(id: number): Observable<IVoiture> {
    return this.httpClient.get<IVoiture>(`${this.resourceUrlS}/${id}`);
  }

  public getListCategorie(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.resourceUrlCategorie}`);
  }

  public getVoitureCategorie(categorie: string): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrlVoitureCategorie}/${categorie}`);
  }

  public getFiltrePrixMax(max: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrlPMax}/${max}`);
  }

  public getFiltrePrixMin(min: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrlPMin}/${min}`);
  }

  public getFiltrePrixIntervalle(min: number, max: number): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrlPInter}/${min}/${max}`);
  }

  public getFiltreEtat(etat: Etat): Observable<IVoiture[]> {
    return this.httpClient.get<IVoiture[]>(`${this.resourceUrlEtat}/${etat}`);
  }
}
