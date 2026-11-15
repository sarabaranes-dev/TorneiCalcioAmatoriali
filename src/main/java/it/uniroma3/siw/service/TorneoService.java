package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.RigaClassifica;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TorneoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TorneoService {
	
	@Autowired
	private TorneoRepository torneoRepository;
	
	@Autowired
	private PartecipazioneRepository partecipazioneRepository;
	
	@Autowired
	private SquadraRepository squadraRepository;
	
	public boolean existsByNomeAndAnno(String nome, Integer anno) {
		return torneoRepository.existsByNomeAndAnno(nome, anno);
	}
	
	// Caso d'uso: visualizzazione dell’elenco dei tornei
    @Transactional(readOnly = true)
	public Iterable<Torneo> findAll() {
		return torneoRepository.findAll();
	}
	
	// Caso d'uso: visualizzazione del dettaglio di un torneo
	@Transactional(readOnly = true)
    public Torneo findById(Long id) {
		return torneoRepository.findById(id).orElse(null);
	}

    @Transactional(readOnly = true)
    public Torneo findByIdWithPartite(Long id) {
        return torneoRepository.findByIdWithPartiteAndSquadre(id).orElse(null);
    }
	
	// Caso d'uso: visualizzazione delle squadre partecipanti
    @Transactional(readOnly = true)
    public List<Squadra> findSquadreByTorneoId(Long idTorneo) {
    	return this.partecipazioneRepository.findSquadreInTorneo(idTorneo);
    }
    
    
    // Caso d'uso: visualizzazione della classifica del torneo
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<Partecipazione> getClassificaTorneo(Long idTorneo) {
    	return this.partecipazioneRepository.findClassificaByTorneoId(idTorneo);
        
    }
    
    // Caso d'uso: classifica con giocate, vinte, pari, perse e gol.
    // Ordine: punti, poi differenza reti, poi gol fatti.
    @Transactional(readOnly = true)
    public List<RigaClassifica> getClassificaDettagliata(Long idTorneo) {
        Map<Squadra, RigaClassifica> righe = new LinkedHashMap<>();
        for (Partecipazione p : this.partecipazioneRepository.findClassificaByTorneoId(idTorneo)) {
            righe.put(p.getSquadra(), new RigaClassifica(p.getSquadra(), p.getPunti() != null ? p.getPunti() : 0));
        }
        Torneo torneo = this.torneoRepository.findByIdWithPartiteAndSquadre(idTorneo).orElse(null);
        if (torneo != null && torneo.getPartite() != null) {
            for (Partita partita : torneo.getPartite()) {
                if (partita.getStato() != Partita.StatoPartita.PLAYED
                        || partita.getGoalsHome() == null || partita.getGoalsAway() == null) continue;
                RigaClassifica casa = righe.get(partita.getSquadraCasa());
                RigaClassifica ospite = righe.get(partita.getSquadraOspite());
                if (casa != null) casa.aggiungiRisultato(partita.getGoalsHome(), partita.getGoalsAway());
                if (ospite != null) ospite.aggiungiRisultato(partita.getGoalsAway(), partita.getGoalsHome());
            }
        }
        List<RigaClassifica> classifica = new ArrayList<>(righe.values());
        classifica.sort(Comparator.comparingInt(RigaClassifica::getPunti)
                .thenComparingInt(RigaClassifica::getDifferenzaReti)
                .thenComparingInt(RigaClassifica::getGolFatti)
                .reversed());
        return classifica;
    }

    // Caso d'uso: calendario del torneo in ordine di data
    @Transactional(readOnly = true)
    public List<Partita> getPartiteOrdinate(Torneo torneo) {
        List<Partita> partite = new ArrayList<>();
        if (torneo != null && torneo.getPartite() != null) partite.addAll(torneo.getPartite());
        partite.sort(Comparator.comparing(Partita::getDataEora, Comparator.nullsLast(Comparator.naturalOrder())));
        return partite;
    }

    @Transactional(readOnly = true)
    public long count() {
        return this.torneoRepository.count();
    }

    // Caso d'uso: creazione e modifica + eliminazione di un torneo
    @Transactional
    public void saveTorneo(Torneo torneo) {
        this.torneoRepository.save(torneo);
    }

    
    //// Caso d'uso: iscrizione squadra ad un torneo
    /** evita iscrizioni concorrenti che violerebbero i vincoli di integrità del torneo.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggiungiSquadraATorneo(Long idTorneo, Long idSquadra) {
        Torneo torneo = this.torneoRepository.findById(idTorneo).orElse(null);
        Squadra squadra = this.squadraRepository.findById(idSquadra).orElse(null);
        
        if (torneo != null && squadra != null) {
            Partecipazione p = new Partecipazione();
            p.setTorneo(torneo);
            p.setSquadra(squadra);
            p.setPunti(0);
            this.partecipazioneRepository.save(p);
        }
    }
}
