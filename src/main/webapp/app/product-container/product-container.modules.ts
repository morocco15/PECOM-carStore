import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

import { SharedModule } from 'app/shared/shared.module'; 
import { ProductContainerComponent } from './product-container.component';
import { ProductContainerService } from './product-container.service';

@NgModule({
  imports: [SharedModule],
  declarations: [ProductContainerComponent] ,
  providers: [ProductContainerService,ApplicationConfigService],
  exports: [ProductContainerComponent]
   
})
export class ProductContainerModule {}