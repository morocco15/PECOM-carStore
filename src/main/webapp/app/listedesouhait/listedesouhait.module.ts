import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { LISTEDESOUHAIT_ROUTE } from './listedesouhait.route';
import { ListedesouhaitComponent } from './listedesouhait.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([LISTEDESOUHAIT_ROUTE])],
  declarations: [ListedesouhaitComponent],
})
export class ListedesouhaitModule {}
