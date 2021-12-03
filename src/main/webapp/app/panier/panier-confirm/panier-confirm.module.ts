import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PANIERCONFIRM_ROUTE } from './panier-confirm.route';
import { PanierConfirmComponent } from './panier-confirm.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([PANIERCONFIRM_ROUTE])],
  declarations: [PanierConfirmComponent],
  //providers: [HomeService],
})
export class PanierConfirmModule {}
