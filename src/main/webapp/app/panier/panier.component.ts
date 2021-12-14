import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Observable } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PanierService } from './panier.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';
import {SouhaitService} from "../listedesouhait/listedesouhait.service";
@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  username!: string;
  voitures!: IVoiture[];
  voitureId!: number;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private panierservice: PanierService,
    private souhaitService:SouhaitService,
    private http: HttpClient
  ) {}

  trackId(index: number, item: IPanier): number {
    return item.id!;
  }

  getPanier(): void {
    // eslint-disable-next-line no-console
    console.log('this.voitures.length');
    this.panierservice.getVoituresDuPanier(this.username).subscribe((res: IVoiture[]) => {
      console.error(res);
      this.voitures = res;
      // eslint-disable-next-line no-console
      console.log(this.voitures.length);
    });
  }

  supprimerVoitureChoisite(idVoiture: number | undefined): void {
    // eslint-disable-next-line eqeqeq
    if (idVoiture != undefined) {
      this.panierservice.supprimerVoitureDuPanier(this.username, idVoiture).subscribe((res: boolean) => {
        console.error(res);
        // eslint-disable-next-line no-console
        console.log('affichier le resultat du suppression:');
        // eslint-disable-next-line no-console
        console.log(res);
        if (res) {
          this.getPanier();
        }
      });
    }
  }

  deplaceAuSouhait(id:number|undefined):void{

    // eslint-disable-next-line eqeqeq
    if(id!=undefined){
      this.voitureId= id;
      this.supprimerVoitureChoisite(this.voitureId);
      this.souhaitService.ajouterVoitureSouhait(this.username,this.voitureId).subscribe((res:boolean)=>{
        console.error(res);
        // eslint-disable-next-line no-console
        console.log(res);
      })
    }
  }
  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    if (this.account) {
      this.username = this.account.login;
    }
    this.getPanier();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
