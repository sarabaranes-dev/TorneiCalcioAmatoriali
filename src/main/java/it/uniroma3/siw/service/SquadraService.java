package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

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
	@Transactional 
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
	
    
	@Transactional
	public void testLazy(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("LAZY");
		Squadra s = squadraRepository.findById(id).get();
		s.getGiocatori().size(); // forza il lazy loading
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	@Transactional
	public void testJoinFetch(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("JOIN FETCH");
		Squadra s = squadraRepository.findByIdWithGiocatori(id);
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	@Transactional
	public void testEntityGraph(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("ENTITY GRAPH");
		Squadra s = squadraRepository.findByIdEntityGraph(id);
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

}
