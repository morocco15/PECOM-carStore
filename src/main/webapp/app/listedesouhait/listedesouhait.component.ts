import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, timer } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';
import { PanierService } from '../panier/panier.service';
import { SouhaitService } from './listedesouhait.service';
import { HomeService } from '../home/home.service';
import { MatDialog } from '@angular/material/dialog';
import { HintComponent } from 'app/hint/hint.component';

@Component({
  selector: 'jhi-listedesouhait',
  templateUrl: './listedesouhait.component.html',
  styleUrls: ['./listedesouhait.component.scss'],
})
export class ListedesouhaitComponent implements OnInit {
  voitures!: IVoiture[];
  account: Account | null = null;
  username!: string;
  voitureChoisit!: IVoiture;
  isEmpty = false;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private souhaitService: SouhaitService,
    private panierService: PanierService,
    private http: HttpClient,
    public dialog: MatDialog
  ) {}

  trackId(index: number, item: IPanier): number {
    return item.id!;
  }

  getSouhait(): void {
    // eslint-disable-next-line no-console
    console.log('this.voitures.length');

    this.souhaitService.getSouhait(this.username).subscribe((res: IVoiture[]) => {
      console.error(res);
      this.voitures = res;
      if (this.voitures.length === 0) {
        this.isEmpty = true;
      }
      // eslint-disable-next-line no-console
      console.log(this.voitures.length);
    });
  }

  supprimerVoiture(id: number | undefined): void {
    // eslint-disable-next-line no-console
    console.log('clic ok');
    // eslint-disable-next-line eqeqeq
    if (id != undefined) {
      this.souhaitService.supprimerVoitureSouhait(this.username, id).subscribe((res: boolean) => {
        console.error(res);
        if (res) {
          this.getSouhait();
          ////
          // this.dialog.open(HintComponent, {data:"Indication: Supprimer dans la liste de souhait !",position:{left:"39rem"}}); 
          // timer(3000) .subscribe(()=>
          // {
          //   this.dialog.closeAll();
          // })
        }

      });
    }
  }

  deplacerAuPanier(voiture: IVoiture): void {
    this.voitureChoisit = voiture;
    this.supprimerVoiture(this.voitureChoisit.id);
    if (this.voitureChoisit.id != null && this.voitureChoisit.version != null) {
      this.panierService
        .ajouterVoiturePanier(this.username, this.voitureChoisit.id, this.voitureChoisit.version)
        .subscribe((res: boolean) => {
          console.error(res);

          if(res === true)
          {
            this.dialog.open(HintComponent, {data:"Indication: Déplacer vers panier !",position:{left:"39rem"}}); 
          }
           

          timer(3000) .subscribe(()=>
          {
            this.dialog.closeAll();
          })
        });

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
    this.getSouhait();
  }
}
