import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, retry } from 'rxjs/operators';
import { Voiture } from '../entities/voiture/voiture.model';

@Injectable()
export class HomeService {
  endpoint = 'http://localhost:8080/api/voiture';
  constructor(private httpClient: HttpClient) {}

  public getQuatreDernieresVoitures(): Observable<Voiture[]> {
    return this.httpClient.get<any>(this.endpoint + '/0/4').pipe(
      map((body: any) => {
        console.log('body=', JSON.stringify(body, null, 2));
        return body.content;
      })
      // catchError(this.handleErrorVoitures)
    );
  }
  /* private handleErrorVoiture(error: HttpErrorResponse{
        console.log('Server error to find voiture: ' + JSON.stringify(error, null, 2));
      }*/
}
/* export interface VoituresResponse {
        err: any;
        Voitures: Voiture[];
      }
      export interface VoitureResponse {
        err: any;
        Voiture: Voiture;
      }*/
/*getData(){
        let 
        return get("/api/voiture");
      }*/
