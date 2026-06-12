import React from 'react';
import type { Partita } from '../types';

const THYMELEAF_URL = 'http://localhost:8080';

interface PartitaCardProps {
  partita: Partita;
}

/**
 * Componente riutilizzabile per visualizzare una singola partita
 * È "stupido" - riceve i dati via props e li disegna
 * Non fa nessuna chiamata API
 */
export const PartitaCard: React.FC<PartitaCardProps> = ({ partita }) => {
  const formatData = (dataEora: string | null) => {
    if (!dataEora) return 'Da definire';
    return new Date(dataEora).toLocaleString();
  };

  return (
    <div className="partita-card">
      <div className="partita-header">
        Torneo: <strong>{partita.torneo?.nome || 'Torneo'}</strong> | Data:{' '}
        {formatData(partita.dataEora)}
      </div>

      <div className="partita-matchup">
        <span className="squadra-name squadra-casa">
          {partita.squadraCasa?.nome || 'Squadra Casa'}
        </span>

        <span className="partita-risultato">
          {partita.stato === 'PLAYED'
            ? `${partita.goalsHome} - ${partita.goalsAway}`
            : 'VS'}
        </span>

        <span className="squadra-name squadra-ospite">
          {partita.squadraOspite?.nome || 'Squadra Ospite'}
        </span>
      </div>

      <div className="partita-footer">
        <span className="stadio-testo">Stadio: {partita.luogo || 'Non definito'}</span>
        <a
          href={`${THYMELEAF_URL}/partite/${partita.id}`}
          className="btn btn-sm btn-accent"
        >
          Dettagli Match →
        </a>
      </div>
    </div>
  );
};
