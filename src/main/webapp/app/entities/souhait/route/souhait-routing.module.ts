import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SouhaitComponent } from '../list/souhait.component';
import { SouhaitDetailComponent } from '../detail/souhait-detail.component';
import { SouhaitUpdateComponent } from '../update/souhait-update.component';
import { SouhaitRoutingResolveService } from './souhait-routing-resolve.service';

const souhaitRoute: Routes = [
  {
    path: '',
    component: SouhaitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SouhaitDetailComponent,
    resolve: {
      souhait: SouhaitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SouhaitUpdateComponent,
    resolve: {
      souhait: SouhaitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SouhaitUpdateComponent,
    resolve: {
      souhait: SouhaitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(souhaitRoute)],
  exports: [RouterModule],
})
export class SouhaitRoutingModule {}
