import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IVoiture } from 'app/entities/voiture/voiture.model';

export interface ISouhait {
  id?: number;
  utilisateur?: IUtilisateur | null;
  voitures?: IVoiture[] | null;
}

export class Souhait implements ISouhait {
  constructor(public id?: number, public utilisateur?: IUtilisateur | null, public voitures?: IVoiture[] | null) {}
}

export function getSouhaitIdentifier(souhait: ISouhait): number | undefined {
  return souhait.id;
}
