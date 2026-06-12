package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.SquadraRepository;
import jakarta.transaction.Transactional;

@Service
public class SquadraService {

	@Autowired
	private SquadraRepository squadraRepository;
	
	public Iterable<Squadra> findAll() {
	    return squadraRepository.findAll();
	}

	public Optional<Squadra> findByNome(String nome) {
		return this.squadraRepository.findByNome(nome);
	}
	
	//Caso d'uso: visualizzazione del dettaglio di una squadra (con giocatori)
	@Transactional //mantiene aperta la connessione al database finché non hai finito di leggere tutti i dati
	public Squadra findById(Long id) {
		return squadraRepository.findById(id).get();
	}

	// Caso d'uso: inserimento e modifica + eliminazione di una squadra
    @Transactional
    public void saveSquadra(Squadra squadra) {
        this.squadraRepository.save(squadra);
    }

    @Transactional
    public void deleteSquadra(Long id) {
        this.squadraRepository.deleteById(id);
    }
    
    public boolean existsByNome(String nome) {
		return squadraRepository.existsByNome(nome);
	}
	
    
	
}
