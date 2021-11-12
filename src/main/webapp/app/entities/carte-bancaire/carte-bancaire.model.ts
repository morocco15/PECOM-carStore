import * as dayjs from 'dayjs';

export interface ICarteBancaire {
  id?: number;
  code?: number | null;
  expiration?: dayjs.Dayjs | null;
  prenom?: string | null;
  nom?: string | null;
}

export class CarteBancaire implements ICarteBancaire {
  constructor(
    public id?: number,
    public code?: number | null,
    public expiration?: dayjs.Dayjs | null,
    public prenom?: string | null,
    public nom?: string | null
  ) {}
}

export function getCarteBancaireIdentifier(carteBancaire: ICarteBancaire): number | undefined {
  return carteBancaire.id;
}
