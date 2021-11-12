import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVendeur } from '../vendeur.model';
import { VendeurService } from '../service/vendeur.service';
import { VendeurDeleteDialogComponent } from '../delete/vendeur-delete-dialog.component';

@Component({
  selector: 'jhi-vendeur',
  templateUrl: './vendeur.component.html',
})
export class VendeurComponent implements OnInit {
  vendeurs?: IVendeur[];
  isLoading = false;

  constructor(protected vendeurService: VendeurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.vendeurService.query().subscribe(
      (res: HttpResponse<IVendeur[]>) => {
        this.isLoading = false;
        this.vendeurs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVendeur): number {
    return item.id!;
  }

  delete(vendeur: IVendeur): void {
    const modalRef = this.modalService.open(VendeurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vendeur = vendeur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
