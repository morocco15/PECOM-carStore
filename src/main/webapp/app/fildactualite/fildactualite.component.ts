import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { HomeService } from 'app/home/home.service';
import { Etat } from 'app/entities/enumerations/etat.model';

@Component({
  selector: 'jhi-fildactualite',
  templateUrl: './fildactualite.component.html',
  styleUrls: ['./fildactualite.component.scss'],
})
export class FildactualiteComponent implements OnInit {
  static voitureid: number;
  account: Account | null = null;
  voitures!: IVoiture[];
  username!: string;
  debut = 0;
  fin = 10;
  coef = 1;
  sendURL!: string;
  listCategorie!: string[];
  affichageCategorie1 = false;
  affichageCategorie2 = true;
  labelPrixMax = true;
  labelPrixMin = false;
  labelPrixIntervalle = false;
  filtrage = 1;
  valeur1!: number;
  valeur2!: number;
  modepasFiltre = true;
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private homeservice: HomeService, private http: HttpClient) {}

  filtre(index: number): void {
    switch (index) {
      case 1:
        this.filtrage = 1;
        this.labelPrixMax = true;
        this.labelPrixMin = false;
        this.labelPrixIntervalle = false;
        break;
      case 2:
        this.filtrage = 2;
        this.labelPrixMax = false;
        this.labelPrixMin = true;
        this.labelPrixIntervalle = false;
        break;
      case 3:
        this.filtrage = 3;
        this.labelPrixMax = false;
        this.labelPrixMin = false;
        this.labelPrixIntervalle = true;
        break;
      case 4:
        this.filtrage = 4;
        this.labelPrixMax = false;
        this.labelPrixMin = false;
        this.labelPrixIntervalle = false;
        break;
      case 5:
        this.filtrage = 5;
        this.labelPrixMax = false;
        this.labelPrixMin = false;
        this.labelPrixIntervalle = false;
        break;
      case 6:
        this.filtrage = 6;
        this.labelPrixMax = false;
        this.labelPrixMin = false;
        this.labelPrixIntervalle = false;
        break;

      default:
        break;
    }
  }

  btnFiltrer(i: number, valeur1: number, valeur2: number): void {
    this.modepasFiltre = false;
    switch (i) {
      case 1:
        this.homeservice.getFiltrePrixMax(valeur1).subscribe((res: IVoiture[]) => {
          this.voitures = res;
        });
        break;
      case 2:
        this.homeservice.getFiltrePrixMin(valeur1).subscribe((res: IVoiture[]) => {
          this.voitures = res;
        });
        break;
      case 3:
        this.homeservice.getFiltrePrixIntervalle(valeur1, valeur2).subscribe((res: IVoiture[]) => {
          this.voitures = res;
        });
        break;
      case 4:
        this.homeservice.getFiltreEtat(Etat.ROULANT).subscribe((res: IVoiture[]) => {
          this.voitures = res;
        });
        break;
      case 5:
        this.homeservice.getFiltreEtat(Etat.ENDOMMAGE).subscribe((res: IVoiture[]) => {
          this.voitures = res;
        });
        break;
      case 6:
        this.homeservice.getFiltreEtat(Etat.NONROULANT).subscribe((res: IVoiture[]) => {
          this.voitures = res;
        });
        break;
      default:
        break;
    }
  }

  callService(): void {
    this.modepasFiltre = true;
    this.affichageCategorie1 = false;
    this.affichageCategorie2 = true;
    this.homeservice.getVoituresRecentes(this.debut, this.fin).subscribe((res: IVoiture[]) => {
      this.voitures = res;
      this.coef = 1;
    });
    this.homeservice.getListCategorie().subscribe((res: string[]) => {
      this.listCategorie = res;
    });
  }

  trackId(index: number, item: IVoiture): number {
    return item.id!;
  }

  afficherplus(): void {
    this.homeservice.getVoituresRecentes(this.debut, this.fin + this.coef * 3).subscribe((res: IVoiture[]) => {
      this.voitures = res;
      this.coef++;
    });
  }

  getVoitureDeCategorie(categorie: string): void {
    this.affichageCategorie1 = true;
    this.affichageCategorie2 = false;
    this.homeservice.getVoitureCategorie(categorie).subscribe((res: IVoiture[]) => {
      this.voitures = res;
    });
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
