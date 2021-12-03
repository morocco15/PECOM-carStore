import { Route } from '@angular/router';

import { PanierConfirmComponent } from './panier-confirm.component';

export const PANIERCONFIRM_ROUTE: Route = {
  path: '',
  component: PanierConfirmComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
