import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PANIER_ROUTE } from './panier.route';
import { PanierComponent } from './panier.component';
import { PanierService } from './panier.service';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([PANIER_ROUTE])],
  declarations: [PanierComponent],
  providers: [PanierService],
})
export class PanierModule {}
