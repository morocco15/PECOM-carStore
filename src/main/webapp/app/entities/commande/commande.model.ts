import * as dayjs from 'dayjs';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { Livraison } from 'app/entities/enumerations/livraison.model';

export interface ICommande {
  id?: number;
  dateCommande?: dayjs.Dayjs | null;
  modeLivraison?: Livraison | null;
  voitures?: IVoiture[] | null;
  acheteur?: IUtilisateur | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public dateCommande?: dayjs.Dayjs | null,
    public modeLivraison?: Livraison | null,
    public voitures?: IVoiture[] | null,
    public acheteur?: IUtilisateur | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
