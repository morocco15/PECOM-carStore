import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IVoiture } from 'app/entities/voiture/voiture.model';

export interface IPanier {
  id?: number;
  utilisateur?: IUtilisateur | null;
  voitures?: IVoiture[] | null;
}

export class Panier implements IPanier {
  constructor(public id?: number, public utilisateur?: IUtilisateur | null, public voitures?: IVoiture[] | null) {}
}

export function getPanierIdentifier(panier: IPanier): number | undefined {
  return panier.id;
}
