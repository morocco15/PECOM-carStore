import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Livraison } from 'app/entities/enumerations/livraison.model';
import { HomeService } from 'app/home/home.service';
import { HintComponent } from 'app/hint/hint.component';
import { PanierComponent } from 'app/panier/panier.component';
import { timer } from 'rxjs';

@Component({
  selector: 'jhi-bancaire',
  templateUrl: './bancaire.component.html',
  styleUrls: ['./bancaire.component.scss'],
})
export class BancaireComponent implements OnInit {
  resultat?: number;
  constructor(private router: Router, private homeservice: HomeService, public dialog: MatDialog) {
    //
  }

  validerPaiement(): void {
    this.homeservice.payerPanier(PanierComponent.username, Livraison.LIVRER).subscribe((res: number) => {
      this.resultat = res;
      //eslint-disable-next-line no-console
      console.log(res);
      //eslint-disable-next-line no-console
      console.log(this.resultat);
      if (res === 200) {
        timer(2000).subscribe(() => {
          this.router.navigate(['/confirmation']);
        });
      } else {
        timer(3000).subscribe(() => {
          this.router.navigate(['/']);
        });
      }
    });
  }

  retour(): void {
    this.router.navigate(['panier/panierconfirm']);
  }

  ngOnInit(): void {
    //
  }
}
