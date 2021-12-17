import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PanierConfirmComponent } from '././panier/panier-confirm/panier-confirm.component';
import { ArticleComponent } from './article/article.component';
import { AdresseComponent } from './adresse/adresse.component';
import { BancaireComponent } from './bancaire/bancaire.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'adresse',
          component: AdresseComponent,
        },

        {
          path: 'bancaire',
          component: BancaireComponent,
        },
        {
          path: 'panier/panierconfirm',
          component: PanierConfirmComponent,
        },
        {
          path: 'confirmation',
          component: ConfirmationComponent,
        },
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'article',
          loadChildren: () => import('./article/article.module').then(m => m.ArticleModule),
        },
        {
          path: 'fildactualite',
          loadChildren: () => import('./fildactualite/fildactualite.module').then(m => m.FildactualiteModule),
        },
        {
          path: 'panier',
          loadChildren: () => import('./panier/panier.module').then(m => m.PanierModule),
        },
        {
          path: 'listedesouhait',
          loadChildren: () => import('./listedesouhait/listedesouhait.module').then(m => m.ListedesouhaitModule),
        },
        {
          path: 'panierconfirm',
          loadChildren: () => import('././panier/panier-confirm/panier-confirm.module').then(m => m.PanierConfirmModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },
        ...LAYOUT_ROUTES,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
