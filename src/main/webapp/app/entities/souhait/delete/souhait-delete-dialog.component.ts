import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISouhait } from '../souhait.model';
import { SouhaitService } from '../service/souhait.service';

@Component({
  templateUrl: './souhait-delete-dialog.component.html',
})
export class SouhaitDeleteDialogComponent {
  souhait?: ISouhait;

  constructor(protected souhaitService: SouhaitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.souhaitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
