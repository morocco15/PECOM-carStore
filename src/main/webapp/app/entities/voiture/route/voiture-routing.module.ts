import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VoitureComponent } from '../list/voiture.component';
import { VoitureDetailComponent } from '../detail/voiture-detail.component';
import { VoitureUpdateComponent } from '../update/voiture-update.component';
import { VoitureRoutingResolveService } from './voiture-routing-resolve.service';

const voitureRoute: Routes = [
  {
    path: '',
    component: VoitureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VoitureDetailComponent,
    resolve: {
      voiture: VoitureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VoitureUpdateComponent,
    resolve: {
      voiture: VoitureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VoitureUpdateComponent,
    resolve: {
      voiture: VoitureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(voitureRoute)],
  exports: [RouterModule],
})
export class VoitureRoutingModule {}
