import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Observable, timer } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HomeService } from './home.service';

import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
//import {PanierService} from "../panier/panier.service";
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { HintComponent } from 'app/hint/hint.component';

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
    private http: HttpClient,
    public dialog: MatDialog
  ) {}

  callService(): void {
    this.homeservice.getVoituresRecentes(0, 4).subscribe((res: IVoiture[]) => {
      //eslint-disable-next-line no-console
      console.log('bonjour');
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
          //eslint-disable-next-line no-console
          console.error(res);
          // eslint-disable-next-line no-console
          console.log(res);

          if (res === true) {
            this.dialog.open(HintComponent, { data: 'Indication: Ajouter dans le panier !', position: { top: '-29rem', left: '40rem' } });
          } else {
            this.dialog.open(HintComponent, {
              data: 'Indication: Déja réservé dans le panier !',
              position: { top: '-29rem', left: '40rem' },
            });
          }

          timer(2000).subscribe(() => {
            this.dialog.closeAll();
          });
        });
    }
  }

  btnActionSouhait(voiture: IVoiture): void {
    if (voiture.id != null) {
      this.souhaitservice.ajouterVoitureSouhait(this.username, voiture.id).subscribe((res: boolean) => {
        // eslint-disable-next-line no-console
        console.log(res);

        if (res === true) {
          this.dialog.open(HintComponent, {
            data: 'Indication: Ajouter dans la list de souhait !',
            position: { top: '-29rem', left: '40rem' },
          });
        } else {
          this.dialog.open(HintComponent, {
            data: 'Indication: Déja ajouté dans la liste de souhait !',
            position: { top: '-29rem', left: '40rem' },
          });
        }

        timer(2000).subscribe(() => {
          this.dialog.closeAll();
        });

        //console.log(res);
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
