import { Component, OnInit } from '@angular/core';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

import { PanierService } from '../panier.service';
import { AccountService } from 'app/core/auth/account.service';

import { Account } from 'app/core/auth/account.model';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';

@Component({
  selector: 'jhi-panier-confirm',
  templateUrl: './panier-confirm.component.html',
  styleUrls: ['./panier-confirm.component.scss'],
})
export class PanierConfirmComponent implements OnInit {
  paniers?: IPanier[];
  username!: string;
  voitures!: IVoiture[];
  account: Account | null = null;
  sommeTotale!: number;
  i!: number;
  private readonly destroy$ = new Subject<void>();

  constructor(private panierservice: PanierService, private accountService: AccountService) {}

  trackId(index: number, item: IPanier): number {
    return item.id!;
  }

  getPanier(): void {
    this.panierservice.getVoituresDuPanier(this.username).subscribe((res: IVoiture[]) => {
      this.voitures = res;
      for (this.i = 0; this.i < this.voitures.length; this.i++) {
        this.sommeTotale = this.sommeTotale + this.voitures[this.i].prix!;
      }
    });
  }

  ngOnInit(): void {
    this.sommeTotale = 0;
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    if (this.account) {
      this.username = this.account.login;
    }
    this.getPanier();
  }

  handlePay(): void {
    //
  }
}
