import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Observable } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PanierService } from './panier.service';
import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';

@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit {
  account: Account | null = null;
  voiture1!: IVoiture;
  voiture2!: IVoiture;
  voiture3!: IVoiture;
  voiture4!: IVoiture;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private panierservice: PanierService,
    private http: HttpClient
  ) {}

  callService(): void {
    this.panierservice.getQuatreDernieresVoitures(0, 4).subscribe((res: IVoiture[]) => {
      //eslint-disable-next-line no-console
      console.error(res);
      this.voiture1 = res[0];
      this.voiture2 = res[1];
      this.voiture3 = res[2];
      this.voiture4 = res[3];
    });
  }

  ngOnInit(): void {
    //
  }

  /*test(): void{
    //var toutSelectionne = document.getElementById("toutselectionne");
    //var voiture1 = document.getElementById("voiture1");
    //var voiture2 = document.getElementById("voiture2");
    //var toutSelectionne = document.querySelector('input[value="toutselectionne"]');
    var voiture1 = document.querySelector('input[value="voiture1"]');
    var voiture2 = document.querySelector('input[value="voiture2"]');
    //if (document.querySelector('input[value="toutselectionne"]').checked === true){
    if (document.getElementById("toutselectionne").checked === true){
      voiture1.checked=true;
      voiture2.checked=true;
    }
    else{
      voiture1.checked=false;
      voiture2.checked=false;
    }
  }*/
}
