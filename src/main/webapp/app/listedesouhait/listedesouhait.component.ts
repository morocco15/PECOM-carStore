import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HttpClient } from '@angular/common/http';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IPanier } from 'app/entities/panier/panier.model';

@Component({
  selector: 'jhi-listedesouhait',
  templateUrl: './listedesouhait.component.html',
  styleUrls: ['./listedesouhait.component.scss'],
})
export class ListedesouhaitComponent implements OnInit {
  voitures!: IVoiture[];
  constructor() {
    //
  }

  trackId(index: number, item: IPanier): number {
    return item.id!;
  }

  ngOnInit(): void {
    //
  }
}
