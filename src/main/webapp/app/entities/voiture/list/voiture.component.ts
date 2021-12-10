import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVoiture } from '../voiture.model';
import { VoitureService } from '../service/voiture.service';
import { VoitureDeleteDialogComponent } from '../delete/voiture-delete-dialog.component';

@Component({
  selector: 'jhi-voiture',
  templateUrl: './voiture.component.html',
})
export class VoitureComponent implements OnInit {
  voitures?: IVoiture[];
  isLoading = false;

  constructor(protected voitureService: VoitureService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.voitureService.query().subscribe(
      (res: HttpResponse<IVoiture[]>) => {
        this.isLoading = false;
        this.voitures = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVoiture): number {
    return item.id!;
  }

  delete(voiture: IVoiture): void {
    const modalRef = this.modalService.open(VoitureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.voiture = voiture;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
