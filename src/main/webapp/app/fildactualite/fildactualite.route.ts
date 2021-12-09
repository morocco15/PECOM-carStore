import { Route } from '@angular/router';

import { FildactualiteComponent } from './fildactualite.component';

export const FILDACTUALITE_ROUTE: Route = {
  path: '',
  component: FildactualiteComponent,
  data: {
    pageTitle: "Fil d'actualité",
  },
};
