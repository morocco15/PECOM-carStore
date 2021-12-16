import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PanierService } from './panier.service';
import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';
import { SouhaitService } from '../listedesouhait/listedesouhait.service';
import { FildactualiteComponent } from 'app/fildactualite/fildactualite.component';

@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit, OnDestroy {
  static username: string;
  account: Account | null = null;
  voitures!: IVoiture[];
  voitureId!: number;
  isEmpty = false;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private panierservice: PanierService,
    private souhaitService: SouhaitService
  ) {}

  trackId(id: number, item: IPanier): number {
    return item.id!;
  }

  getPanier(): void {
    this.panierservice.getVoituresDuPanier(PanierComponent.username).subscribe((res: IVoiture[]) => {
      this.voitures = res;
      if (this.voitures.length === 0) {
        this.isEmpty = true;
      }
    });
  }

  supprimerVoitureChoisite(idVoiture: number | undefined): void {
    if (idVoiture !== undefined) {
      this.panierservice.supprimerVoitureDuPanier(PanierComponent.username, idVoiture).subscribe((res: boolean) => {
        //
        if (res) {
          this.getPanier();
        }
      });
    }
  }

  deplaceAuSouhait(id: number | undefined): void {
    if (id !== undefined) {
      this.voitureId = id;
      this.supprimerVoitureChoisite(this.voitureId);
      this.souhaitService.ajouterVoitureSouhait(PanierComponent.username, this.voitureId).subscribe((res: boolean) => {
        //
      });
    }
  }

  ficheproduit(id: number): void {
    FildactualiteComponent.voitureid = id;
    this.router.navigate(['/article']);
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    if (this.account) {
      PanierComponent.username = this.account.login;
    }
    this.getPanier();
    //eslint-disable-next-line no-console
    console.log(PanierComponent.username);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
