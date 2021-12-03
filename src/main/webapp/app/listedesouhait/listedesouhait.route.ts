import { Route } from '@angular/router';

import { ListedesouhaitComponent } from './listedesouhait.component';

export const LISTEDESOUHAIT_ROUTE: Route = {
  path: '',
  component: ListedesouhaitComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
