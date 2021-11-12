import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VoitureComponent } from './list/voiture.component';
import { VoitureDetailComponent } from './detail/voiture-detail.component';
import { VoitureUpdateComponent } from './update/voiture-update.component';
import { VoitureDeleteDialogComponent } from './delete/voiture-delete-dialog.component';
import { VoitureRoutingModule } from './route/voiture-routing.module';

@NgModule({
  imports: [SharedModule, VoitureRoutingModule],
  declarations: [VoitureComponent, VoitureDetailComponent, VoitureUpdateComponent, VoitureDeleteDialogComponent],
  entryComponents: [VoitureDeleteDialogComponent],
})
export class VoitureModule {}
