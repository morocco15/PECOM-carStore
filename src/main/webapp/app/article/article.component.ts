import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IVoiture } from '../entities/voiture/voiture.model';
import { AccountService } from '../core/auth/account.service';
import { Router } from '@angular/router';
import { HomeService } from '../home/home.service';
import { PanierService } from '../panier/panier.service';
import { SouhaitService } from '../listedesouhait/listedesouhait.service';
import { HttpClient } from '@angular/common/http';
import { Subject, timer } from 'rxjs';
import { Account } from '../core/auth/account.model';
import { takeUntil } from 'rxjs/operators';
import { FildactualiteComponent } from 'app/fildactualite/fildactualite.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { HintComponent } from 'app/hint/hint.component';

@Component({
  selector: 'jhi-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss'],
})
export class ArticleComponent implements OnInit {
  click_account = 0;
  account: Account | null = null;
  username!: string;
  voitureChoisit!: IVoiture;
  getURL!: string;
  value!: string;
  voiture!: IVoiture;
  avancer = 1;
  afficher1 = true;
  afficher2 = false;
  afficher3 = false;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private panierservice: PanierService,
    private souhaitservice: SouhaitService,
    private homeservice: HomeService,
    private http: HttpClient,
    public dialog: MatDialog
  ) {}

  AjouterPanier(): void {
    if (this.voiture.id != null && this.voiture.version != null) {
      this.panierservice.ajouterVoiturePanier(this.username, this.voiture.id, this.voiture.version).subscribe((res: boolean) => {
        // eslint-disable-next-line no-console
        console.log(res);
        // eslint-disable-next-line eqeqeq
        if (res == true) {
          // eslint-disable-next-line no-console
          console.log('1');
          this.dialog.open(HintComponent, {
            data: 'Ajouté dans votre panier !',
            position: { left: '40rem' },
          });
        } else {
          // eslint-disable-next-line no-console
          console.log('2');
          this.dialog.open(HintComponent, {
            data: 'Déjà dans votre panier !',
            position: { left: '40rem' },
          });
        }

        timer(1500).subscribe(() => {
          this.dialog.closeAll();
        });
      });
      this.router.navigate(['panier']);
    }
  }

  AjouterSouhait(): void {
    if (this.voiture.id != null) {
      this.souhaitservice.ajouterVoitureSouhait(this.username, this.voiture.id).subscribe((res: boolean) => {
        // eslint-disable-next-line no-console
        console.log(res);
        // eslint-disable-next-line eqeqeq
        if (res == true) {
          // eslint-disable-next-line no-console
          console.log('1');
          this.dialog.open(HintComponent, {
            data: 'Ajouté dans vos favoris !',
            position: { left: '40rem' },
          });
        } else {
          // eslint-disable-next-line no-console
          console.log('2');
          this.dialog.open(HintComponent, {
            data: 'Déjà dans vos favoris !',
            position: { left: '40rem' },
          });
        }

        timer(1500).subscribe(() => {
          this.dialog.closeAll();
        });
      });
    }
  }

  getValue(): void {
    this.homeservice.getVoituresByID(FildactualiteComponent.voitureid).subscribe((res: IVoiture) => {
      this.voiture = res;
    });
  }

  ngOnInit(): void {
    //eslint-disable-next-line no-console
    console.log(this.avancer);
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

  nextSlide(): void {
    //eslint-disable-next-line no-console
    console.log(this.avancer);
    this.avancer = this.avancer + 1;
    // eslint-disable-next-line eqeqeq
    if (this.avancer > 3) {
      this.avancer = 1;
    }
    //eslint-disable-next-line no-console
    console.log(this.avancer);
    switch (this.avancer) {
      case 1:
        this.afficher1 = true;
        this.afficher2 = false;
        this.afficher3 = false;
        break;
      case 2:
        this.afficher1 = false;
        this.afficher2 = true;
        this.afficher3 = false;
        break;
      case 3:
        this.afficher1 = false;
        this.afficher2 = false;
        this.afficher3 = true;
        break;
      default:
        break;
    }
  }

  preSlide(): void {
    //eslint-disable-next-line no-console
    console.log(this.avancer);
    this.avancer = this.avancer - 1;
    // eslint-disable-next-line eqeqeq
    if (this.avancer < 1) {
      this.avancer = 3;
    }
    //eslint-disable-next-line no-console
    console.log(this.avancer);
    switch (this.avancer) {
      case 1:
        this.afficher1 = true;
        this.afficher2 = false;
        this.afficher3 = false;
        break;
      case 2:
        this.afficher1 = false;
        this.afficher2 = true;
        this.afficher3 = false;
        break;
      case 3:
        this.afficher1 = false;
        this.afficher2 = false;
        this.afficher3 = true;
        break;
      default:
        break;
    }
  }
}
