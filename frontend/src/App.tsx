import './App.css';
import { GestionePartitePage } from './pages/GestionePartitePage';

/**
 * Componente principale dell'applicazione
 * 
 * Struttura (seguendo le indicazioni del prof):
 * - types/ → Il "vocabolario" TypeScript
 * - services/ → I "camerieri" che comunicano con Spring Boot
 * - components/ → I "mattoncini LEGO" grafici e riutilizzabili
 * - pages/ → Il "tavolo" orchestratore che unisce tutto
 * - App.tsx → Il punto di ingresso principale
 */
function App() {
  return <GestionePartitePage />;
}

export default App;