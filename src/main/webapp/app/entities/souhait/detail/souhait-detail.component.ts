import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISouhait } from '../souhait.model';

@Component({
  selector: 'jhi-souhait-detail',
  templateUrl: './souhait-detail.component.html',
})
export class SouhaitDetailComponent implements OnInit {
  souhait: ISouhait | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ souhait }) => {
      this.souhait = souhait;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
