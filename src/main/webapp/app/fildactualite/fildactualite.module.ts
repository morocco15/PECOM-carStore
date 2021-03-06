import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { FILDACTUALITE_ROUTE } from './fildactualite.route';
import { FildactualiteComponent } from './fildactualite.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [SharedModule, FormsModule, RouterModule.forChild([FILDACTUALITE_ROUTE])],
  declarations: [FildactualiteComponent],
})
export class FildactualiteModule {}
