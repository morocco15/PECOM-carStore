import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PanierConfirmComponent } from '././panier/panier-confirm/panier-confirm.component';
import { ArticleComponent } from './article/article.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'panier/panierconfirm',
          component: PanierConfirmComponent,
        },
        {
          path: 'article',
          component: ArticleComponent,
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
          path: 'fildactualite',
          loadChildren: () => import('./fildactualite/fildactualite.module').then(m => m.FildactualiteModule),
        },
        {
          path: 'panier',
          loadChildren: () => import('./panier/panier.module').then(m => m.PanierModule),
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
