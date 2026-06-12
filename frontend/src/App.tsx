import { useEffect, useState, useMemo } from 'react';
import axios from 'axios';
import type { Partita } from './types';
import './App.css';

const THYMELEAF_URL = 'http://localhost:8080';

function App() {
  const [partite, setPartite] = useState<Partita[]>([]);
  const [filtroStato, setFiltroStato] = useState<string>('ALL');

  useEffect(() => {
    axios.get(`${THYMELEAF_URL}/rest/partite`)
      .then(response => setPartite(response.data))
      .catch(error => console.error(error));
  }, []);

  const partiteFiltrate = useMemo(() => {
    if (filtroStato === 'ALL') return partite;
    return partite.filter(p => p.stato === filtroStato);
  }, [partite, filtroStato]);

  return (
    <div className="dashboard-container">
      
      {/* Contenitore per il bottone di ritorno (Senza stili in linea) */}
      <div className="back-home-container">
        <a href={`${THYMELEAF_URL}/`} className="btn btn-sm">
          ← Torna alla Home
        </a>
      </div>

      <h1 className="dashboard-title">Dashboard Torneo</h1>
      <p style={{ textAlign: 'center', color: '#666' }}>
        Calendario dinamico integrato via REST con la piattaforma.
      </p>

      {/* Bottoni di Filtro */}
      <div className="filter-container">
        <button 
          onClick={() => setFiltroStato('ALL')}
          className={`btn-filtro ${filtroStato === 'ALL' ? 'all' : ''}`}
        >
          Tutte ({partite.length})
        </button>
        <button 
          onClick={() => setFiltroStato('SCHEDULED')}
          className={`btn-filtro ${filtroStato === 'SCHEDULED' ? 'scheduled' : ''}`}
        >
          Da Giocare ({partite.filter(p => p.stato === 'SCHEDULED').length})
        </button>
        <button 
          onClick={() => setFiltroStato('PLAYED')}
          className={`btn-filtro ${filtroStato === 'PLAYED' ? 'played' : ''}`}
        >
          Disputate ({partite.filter(p => p.stato === 'PLAYED').length})
        </button>
      </div>

      {/* Lista delle Partite */}
      {partiteFiltrate.length === 0 ? (
        <p style={{ textAlign: 'center', fontStyle: 'italic', color: '#888' }}>
          Nessuna partita trovata per questo filtro.
        </p>
      ) : (
        <div>
          {partiteFiltrate.map(partita => (
            <div key={partita.id} className="partita-card">
              
              <div className="partita-header">
                Torneo: <strong>{partita.torneo?.nome || "Torneo"}</strong> | Data: {partita.dataEora ? new Date(partita.dataEora).toLocaleString() : "Da definire"}
              </div>

              <div className="partita-matchup">
                <span className="squadra-name squadra-casa">
                  {partita.squadraCasa?.nome || "Squadra Casa"}
                </span>
                
                <span className="partita-risultato">
                  {partita.stato === 'PLAYED' ? `${partita.goalsHome} - ${partita.goalsAway}` : 'VS'}
                </span>
                
                <span className="squadra-name squadra-ospite">
                  {partita.squadraOspite?.nome || "Squadra Ospite"}
                </span>
              </div>

              {/* Footer della card completamente ripulito dagli stili inline */}
              <div className="partita-footer">
                <span className="stadio-testo">Stadio: {partita.luogo || "Non definito"}</span>
                <a href={`${THYMELEAF_URL}/partite/${partita.id}`} className="btn btn-sm btn-accent">
                  Dettagli Match →
                </a>
              </div>

            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;