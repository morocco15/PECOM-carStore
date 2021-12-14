import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Observable, Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';

import { HomeService } from 'app/home/home.service';

@Component({
  selector: 'jhi-fildactualite',
  templateUrl: './fildactualite.component.html',
  styleUrls: ['./fildactualite.component.scss'],
})
export class FildactualiteComponent implements OnInit {
  account: Account | null = null;
  voitures!: IVoiture[];
  username!: string;
  debut = 0;
  fin = 10;
  coef = 1;
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private homeservice: HomeService, private http: HttpClient) {}

  callService(): void {
    this.homeservice.getVoituresRecentes(this.debut, this.fin).subscribe((res: IVoiture[]) => {
      //eslint-disable-next-line no-console
      console.error(res);
      this.voitures = res;
      this.coef = 1;
    });
  }

  trackId(index: number, item: IPanier): number {
    return item.id!;
  }

  afficherplus(): void {
    this.homeservice.getVoituresRecentes(this.debut, this.fin + this.coef * 2).subscribe((res: IVoiture[]) => {
      //eslint-disable-next-line no-console
      console.error(res);
      this.voitures = res;
      this.coef++;
    });
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.callService();
    if (this.account) {
      this.username = this.account.login;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
