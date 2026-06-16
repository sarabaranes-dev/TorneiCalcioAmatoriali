package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import it.uniroma3.siw.model.Partecipazione;
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
    
    // Caso d'uso: creazione e modifica + eliminazione di un torneo
    @Transactional
    public void saveTorneo(Torneo torneo) {
        this.torneoRepository.save(torneo);
    }

    @Transactional
    public void deleteTorneo(Long id) {
        this.torneoRepository.deleteById(id);
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
