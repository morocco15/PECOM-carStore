import { IUser } from 'app/entities/user/user.model';
import { ICarteBancaire } from 'app/entities/carte-bancaire/carte-bancaire.model';
import { IPanier } from 'app/entities/panier/panier.model';
import { ISouhait } from 'app/entities/souhait/souhait.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface IUtilisateur {
  id?: number;
  idcompte?: IUser | null;
  idPaiment?: ICarteBancaire | null;
  panier?: IPanier | null;
  souhait?: ISouhait | null;
  commandes?: ICommande[] | null;
}

export class Utilisateur implements IUtilisateur {
  constructor(
    public id?: number,
    public idcompte?: IUser | null,
    public idPaiment?: ICarteBancaire | null,
    public panier?: IPanier | null,
    public souhait?: ISouhait | null,
    public commandes?: ICommande[] | null
  ) {}
}

export function getUtilisateurIdentifier(utilisateur: IUtilisateur): number | undefined {
  return utilisateur.id;
}
