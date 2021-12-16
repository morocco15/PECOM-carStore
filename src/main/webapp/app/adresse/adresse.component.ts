import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-adresse',
  templateUrl: './adresse.component.html',
  styleUrls: ['./adresse.component.scss'],
})
export class AdresseComponent implements OnInit {
  constructor(private router: Router) {
    //
  }

  retour(): void {
    this.router.navigate(['panier/panierconfirm']);
  }

  continuer(): void {
    this.router.navigate(['bancaire']);
  }

  ngOnInit(): void {
    //
  }
}
