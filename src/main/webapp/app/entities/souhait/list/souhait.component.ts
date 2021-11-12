import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISouhait } from '../souhait.model';
import { SouhaitService } from '../service/souhait.service';
import { SouhaitDeleteDialogComponent } from '../delete/souhait-delete-dialog.component';

@Component({
  selector: 'jhi-souhait',
  templateUrl: './souhait.component.html',
})
export class SouhaitComponent implements OnInit {
  souhaits?: ISouhait[];
  isLoading = false;

  constructor(protected souhaitService: SouhaitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.souhaitService.query().subscribe(
      (res: HttpResponse<ISouhait[]>) => {
        this.isLoading = false;
        this.souhaits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISouhait): number {
    return item.id!;
  }

  delete(souhait: ISouhait): void {
    const modalRef = this.modalService.open(SouhaitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.souhait = souhait;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
