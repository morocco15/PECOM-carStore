import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'utilisateur',
        data: { pageTitle: 'Utilisateurs' },
        loadChildren: () => import('./utilisateur/utilisateur.module').then(m => m.UtilisateurModule),
      },
      {
        path: 'carte-bancaire',
        data: { pageTitle: 'CarteBancaires' },
        loadChildren: () => import('./carte-bancaire/carte-bancaire.module').then(m => m.CarteBancaireModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'Commandes' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'vendeur',
        data: { pageTitle: 'Vendeurs' },
        loadChildren: () => import('./vendeur/vendeur.module').then(m => m.VendeurModule),
      },
      {
        path: 'paniers',
        data: { pageTitle: 'Paniers' },
        loadChildren: () => import('./panier/panier.module').then(m => m.PanierModule),
      },
      {
        path: 'souhait',
        data: { pageTitle: 'Souhaits' },
        loadChildren: () => import('./souhait/souhait.module').then(m => m.SouhaitModule),
      },
      {
        path: 'voiture',
        data: { pageTitle: 'Voitures' },
        loadChildren: () => import('./voiture/voiture.module').then(m => m.VoitureModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'marque',
        data: { pageTitle: 'Marques' },
        loadChildren: () => import('./marque/marque.module').then(m => m.MarqueModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
