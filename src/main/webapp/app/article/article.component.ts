import { Component, OnInit } from '@angular/core';
import { IVoiture } from '../entities/voiture/voiture.model';
import { AccountService } from '../core/auth/account.service';
import { Router } from '@angular/router';
import { HomeService } from '../home/home.service';
import { PanierService } from '../panier/panier.service';
import { SouhaitService } from '../listedesouhait/listedesouhait.service';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
import { Account } from '../core/auth/account.model';
import { takeUntil } from 'rxjs/operators';
import { FildactualiteComponent } from 'app/fildactualite/fildactualite.component';

@Component({
  selector: 'jhi-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss'],
})
export class ArticleComponent implements OnInit {
  account: Account | null = null;
  username!: string;
  voitureChoisit!: IVoiture;
  getURL!: string;
  value!: string;
  voiture!: IVoiture;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private panierservice: PanierService,
    private souhaitservice: SouhaitService,
    private homeservice: HomeService,
    private http: HttpClient
  ) {}

  AjouterPanier(): void {
    if (this.voitureChoisit.id != null && this.voitureChoisit.version != null) {
      this.panierservice
        .ajouterVoiturePanier(this.username, this.voitureChoisit.id, this.voitureChoisit.version)
        .subscribe((res: boolean) => {
          //
        });
    }
  }

  AjouterSouhait(): void {
    if (this.voitureChoisit.id != null) {
      this.souhaitservice.ajouterVoitureSouhait(this.username, this.voitureChoisit.id).subscribe((res: boolean) => {
        //
      });
    }
  }

  getValue(): void {
    this.homeservice.getVoituresByID(FildactualiteComponent.voitureid).subscribe((res: IVoiture) => {
      this.voiture = res;
    });
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    if (this.account) {
      this.username = this.account.login;
    }
    this.getValue();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
