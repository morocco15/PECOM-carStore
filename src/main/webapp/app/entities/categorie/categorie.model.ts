import { IVoiture } from 'app/entities/voiture/voiture.model';

export interface ICategorie {
  id?: number;
  categorie?: string | null;
  voitures?: IVoiture[] | null;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public categorie?: string | null, public voitures?: IVoiture[] | null) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
