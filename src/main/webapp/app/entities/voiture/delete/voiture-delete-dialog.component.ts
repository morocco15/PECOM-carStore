import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVoiture } from '../voiture.model';
import { VoitureService } from '../service/voiture.service';

@Component({
  templateUrl: './voiture-delete-dialog.component.html',
})
export class VoitureDeleteDialogComponent {
  voiture?: IVoiture;

  constructor(protected voitureService: VoitureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voitureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
