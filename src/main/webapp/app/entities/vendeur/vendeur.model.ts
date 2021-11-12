import { IVoiture } from 'app/entities/voiture/voiture.model';

export interface IVendeur {
  id?: number;
  login?: string | null;
  coordonnee?: string | null;
  voitures?: IVoiture[] | null;
}

export class Vendeur implements IVendeur {
  constructor(public id?: number, public login?: string | null, public coordonnee?: string | null, public voitures?: IVoiture[] | null) {}
}

export function getVendeurIdentifier(vendeur: IVendeur): number | undefined {
  return vendeur.id;
}
