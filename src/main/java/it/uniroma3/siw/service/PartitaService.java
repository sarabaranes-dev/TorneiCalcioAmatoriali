package it.uniroma3.siw.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.PartitaRepository;
import jakarta.transaction.Transactional;

@Service
public class PartitaService {

	@Autowired
	private PartitaRepository partitaRepository;

	@Autowired
	private PartecipazioneRepository partecipazioneRepository;

	
	//Caso d'uso: visualizzazione del calendario delle partite
	public Iterable<Partita> findAll() {
		return partitaRepository.findAll();
	}
	
	//Caso d'uso:inserimento del risultato di una partita
    @Transactional
    public void aggiornaRisultato(Long idPartita, int goalsCasa, int goalsOspite) {
        Partita partita = this.partitaRepository.findById(idPartita).orElse(null);
        if (partita == null) return;

        // 1. IMPOSTO I GOL SULL'ENTITÀ PARTITA
        partita.setGoalsHome(goalsCasa);
        partita.setGoalsAway(goalsOspite);

        // 2. CAMBIO AUTOMATICAMENTE LO STATO IN PLAYED
        partita.setStato(Partita.StatoPartita.PLAYED);

        // Recupero le partecipazioni per la classifica (Tua logica intatta)
        Partecipazione partCasa = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraCasa(), partita.getTorneo());
        Partecipazione partOspite = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraOspite(), partita.getTorneo());

        if (goalsCasa > goalsOspite) {
            partCasa.setVittoria();
        } else if (goalsOspite > goalsCasa) {
            partOspite.setVittoria();
        } else {
            partCasa.setPareggio();
            partOspite.setPareggio();
        }
        
        this.partitaRepository.save(partita);
    }

	// Caso d'uso: salvataggio ed eliminazione di una partita
    @Transactional
    public void savePartita(Partita partita) {
        this.partitaRepository.save(partita);
    }

    @Transactional
    public void deletePartita(Long id) {
        this.partitaRepository.deleteById(id);
    }
    
    @Transactional
	public Partita findById(Long id) {
		return partitaRepository.findById(id).get();
	}

    public boolean existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(Torneo torneo, Squadra squadraCasa, Squadra squadraOspite, LocalDateTime dataEora) {
		return partitaRepository.existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(torneo, squadraCasa, squadraOspite, dataEora);
	}
	
   
}
	
