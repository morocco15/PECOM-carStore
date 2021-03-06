import * as dayjs from 'dayjs';
import { IMarque } from 'app/entities/marque/marque.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { IPanier } from 'app/entities/panier/panier.model';
import { IVendeur } from 'app/entities/vendeur/vendeur.model';
import { ISouhait } from 'app/entities/souhait/souhait.model';
import { Statut } from 'app/entities/enumerations/statut.model';
import { Etat } from 'app/entities/enumerations/etat.model';
import { BoiteDeVitesse } from 'app/entities/enumerations/boite-de-vitesse.model';
import { Carburant } from 'app/entities/enumerations/carburant.model';

export interface IVoiture {
  id?: number;
  model?: string | null;
  prix?: number | null;
  image1?: string | null;
  image2?: string | null;
  image3?: string | null;
  statut?: Statut | null;
  version?: number | null;
  miseEnVente?: dayjs.Dayjs | null;
  etat?: Etat | null;
  porte?: number | null;
  boiteVitesse?: BoiteDeVitesse | null;
  co2?: number | null;
  chevaux?: number | null;
  carburant?: Carburant | null;
  annees?: number | null;
  ville?: string | null;
  codePostal?: number | null;
  description?: string | null;
  kilometrage?: number | null;
  marque?: IMarque | null;
  categories?: ICategorie[] | null;
  commande?: ICommande | null;
  panier?: IPanier | null;
  vendeur?: IVendeur | null;
  souhaits?: ISouhait[] | null;
}

export class Voiture implements IVoiture {
  constructor(
    public id?: number,
    public model?: string | null,
    public prix?: number | null,
    public image1?: string | null,
    public image2?: string | null,
    public image3?: string | null,
    public statut?: Statut | null,
    public version?: number | null,
    public miseEnVente?: dayjs.Dayjs | null,
    public etat?: Etat | null,
    public porte?: number | null,
    public boiteVitesse?: BoiteDeVitesse | null,
    public co2?: number | null,
    public chevaux?: number | null,
    public carburant?: Carburant | null,
    public annees?: number | null,
    public ville?: string | null,
    public codePostal?: number | null,
    public description?: string | null,
    public kilometrage?: number | null,
    public marque?: IMarque | null,
    public categories?: ICategorie[] | null,
    public commande?: ICommande | null,
    public panier?: IPanier | null,
    public vendeur?: IVendeur | null,
    public souhaits?: ISouhait[] | null
  ) {}
}

export function getVoitureIdentifier(voiture: IVoiture): number | undefined {
  return voiture.id;
}
