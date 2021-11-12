import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SouhaitComponent } from './list/souhait.component';
import { SouhaitDetailComponent } from './detail/souhait-detail.component';
import { SouhaitUpdateComponent } from './update/souhait-update.component';
import { SouhaitDeleteDialogComponent } from './delete/souhait-delete-dialog.component';
import { SouhaitRoutingModule } from './route/souhait-routing.module';

@NgModule({
  imports: [SharedModule, SouhaitRoutingModule],
  declarations: [SouhaitComponent, SouhaitDetailComponent, SouhaitUpdateComponent, SouhaitDeleteDialogComponent],
  entryComponents: [SouhaitDeleteDialogComponent],
})
export class SouhaitModule {}
