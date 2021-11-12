import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVendeur } from '../vendeur.model';

@Component({
  selector: 'jhi-vendeur-detail',
  templateUrl: './vendeur-detail.component.html',
})
export class VendeurDetailComponent implements OnInit {
  vendeur: IVendeur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendeur }) => {
      this.vendeur = vendeur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
