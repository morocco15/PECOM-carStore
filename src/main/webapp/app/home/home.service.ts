import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Voiture } from '../entities/voiture/voiture.model';
import { catchError } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class HomeService {
  urlAPI = 'http://localhost:8080/api/voiture';

  constructor(private httpClient: HttpClient) {}

  getQuatreDernieresVoitures(): Observable<Voiture[]> {
    return this.httpClient.get<Voiture[]>(this.urlAPI + '/0/4'); /*.pipe(
        tap(voiture => print(),
        catchError(this.handleError<Voiture[]>([]))
      // catchError(this.handleErrorVoitures)
    ));*/
  }

  private handleError<T>(result = {} as T) {
    return (error: HttpErrorResponse): Observable<T> => {
      console.error(error);
      return of(result);
    };
  }
}
