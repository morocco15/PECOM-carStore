import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Observable } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HomeService } from './home.service';

import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { PanierService } from '../panier/panier.service';
import { SouhaitService } from '../listedesouhait/listedesouhait.service';
import { FildactualiteComponent } from 'app/fildactualite/fildactualite.component';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  voiture1!: IVoiture;
  voiture2!: IVoiture;
  voiture3!: IVoiture;
  voiture4!: IVoiture;
  username!: string;
  voitureChoisit!: IVoiture;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private homeservice: HomeService,
    private panierservice: PanierService,
    private souhaitservice: SouhaitService,
    private http: HttpClient
  ) {}

  callService(): void {
    this.homeservice.getVoituresRecentes(0, 4).subscribe((res: IVoiture[]) => {
      this.voiture1 = res[0];
      this.voiture2 = res[1];
      this.voiture3 = res[2];
      this.voiture4 = res[3];
    });
  }

  btnAction(voiture: IVoiture): void {
    this.voitureChoisit = voiture;
    if (this.voitureChoisit.id != null && this.voitureChoisit.version != null) {
      this.panierservice
        .ajouterVoiturePanier(this.username, this.voitureChoisit.id, this.voitureChoisit.version)
        .subscribe((res: boolean) => {
          //
        });
    }
  }

  btnActionSouhait(voiture: IVoiture): void {
    if (voiture.id != null) {
      this.souhaitservice.ajouterVoitureSouhait(this.username, voiture.id).subscribe((res: boolean) => {
        //
      });
    }
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

  ficheproduit(id: number): void {
    FildactualiteComponent.voitureid = id;
    this.router.navigate(['/article']);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
