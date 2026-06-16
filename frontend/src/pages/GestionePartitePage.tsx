import { useEffect, useState, useMemo } from 'react';
import { partiteService } from '../services/partiteService';
import { FilterButton } from '../components/FilterButton';
import { PartitaCard } from '../components/PartitaCard';
import type { Partita } from '../types';

const THYMELEAF_URL = 'http://localhost:8080';

/**
 * Pagina principale per la gestione delle partite
 * 
 * 1. Chiama il service per recuperare i dati
 * 2. Gestisce lo stato (filtro)
 * 3. Filtra i dati in base allo stato selezionato
 * 4. Passa i dati ai componenti per farli disegnare
 */
export function GestionePartitePage() {
  const [partite, setPartite] = useState<Partita[]>([]);
  const [filtroStato, setFiltroStato] = useState<string>('ALL');
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Carica le partite all'avvio
  useEffect(() => {
    const caricaPartite = async () => {
      try {
        setLoading(true);
        setError(null);
        const dati = await partiteService.getPartite();
        setPartite(dati);
      } catch (err) {
        setError('Errore nel caricamento delle partite');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    caricaPartite();
  }, []);

  // Filtra le partite in base allo stato selezionato
  const partiteFiltrate = useMemo(() => {
    if (filtroStato === 'ALL') return partite;
    return partite.filter((p) => p.stato === filtroStato);
  }, [partite, filtroStato]);

  return (
    <div className="dashboard-container">
      {/* Bottone di ritorno alla home */}
      <div className="back-home-container">
        <a href={`${THYMELEAF_URL}/`} className="btn btn-sm">
          ← Torna alla Home
        </a>
      </div>

      <h1 className="dashboard-title">Dashboard Torneo</h1>
      <p style={{ textAlign: 'center', color: '#666' }}>
        Calendario
      </p>

      {/*filtri */}
      <div className="filter-container">
        <FilterButton
          label="Tutte"
          stato="ALL"
          filterValue={filtroStato}
          isActive={filtroStato === 'ALL'}
          count={partite.length}
          onClick={() => setFiltroStato('ALL')}
        />
        <FilterButton
          label="Da Giocare"
          stato="SCHEDULED"
          filterValue={filtroStato}
          isActive={filtroStato === 'SCHEDULED'}
          count={partite.filter((p) => p.stato === 'SCHEDULED').length}
          onClick={() => setFiltroStato('SCHEDULED')}
        />
        <FilterButton
          label="Disputate"
          stato="PLAYED"
          filterValue={filtroStato}
          isActive={filtroStato === 'PLAYED'}
          count={partite.filter((p) => p.stato === 'PLAYED').length}
          onClick={() => setFiltroStato('PLAYED')}
        />
      </div>

      {/* Gestione degli stati di caricamento e errore */}
      {loading && (
        <p style={{ textAlign: 'center', fontStyle: 'italic', color: '#888' }}>
          Caricamento partite in corso...
        </p>
      )}

      {error && (
        <p style={{ textAlign: 'center', color: '#d32f2f', fontWeight: 'bold' }}>
          {error}
        </p>
      )}

      {/* Visualizzazione delle partite filtrate */}
      {!loading && !error && (
        <>
          {partiteFiltrate.length === 0 ? (
            <p style={{ textAlign: 'center', fontStyle: 'italic', color: '#888' }}>
              Nessuna partita trovata per questo filtro.
            </p>
          ) : (
            <div>
              {partiteFiltrate.map((partita) => (
                <PartitaCard key={partita.id} partita={partita} />
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}
