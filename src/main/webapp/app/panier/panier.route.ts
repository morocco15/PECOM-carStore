import { Route } from '@angular/router';

import { PanierComponent } from './panier.component';

export const PANIER_ROUTE: Route = {
  path: '',
  component: PanierComponent,
  data: {
    //authorities: ['ROLE_ADMIN', 'ROLE_USER'],
    pageTitle: 'Panier',
  },
};
