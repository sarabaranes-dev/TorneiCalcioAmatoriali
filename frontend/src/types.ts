export interface Torneo {
  id: number;
  nome: string;
  anno: number;
}

export interface Squadra {
  id: number;
  nome: string;
  citta: string;
}

export interface Partita {
  id: number;
  dataEora: string;
  luogo: string;
  goalsHome: number | null;
  goalsAway: number | null;
  stato: 'SCHEDULED' | 'PLAYED';
  torneo: Torneo;
  squadraCasa: Squadra; // Mantiene il nome corretto del tuo DB
  squadraOspite: Squadra; // Mantiene il nome corretto del tuo DB
}