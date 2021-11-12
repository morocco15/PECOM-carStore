import { IVoiture } from 'app/entities/voiture/voiture.model';

export interface IMarque {
  id?: number;
  marque?: string | null;
  voitures?: IVoiture[] | null;
}

export class Marque implements IMarque {
  constructor(public id?: number, public marque?: string | null, public voitures?: IVoiture[] | null) {}
}

export function getMarqueIdentifier(marque: IMarque): number | undefined {
  return marque.id;
}
