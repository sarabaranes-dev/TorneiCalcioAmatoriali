package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.PartitaRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SquadraService {

	@Autowired
	private SquadraRepository squadraRepository;

	@Autowired
	private PartecipazioneRepository partecipazioneRepository;

	@Autowired
	private PartitaRepository partitaRepository;


	@Transactional(readOnly = true)
	public Iterable<Squadra> findAll() {
	    return squadraRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Squadra> findByNome(String nome) {
		return this.squadraRepository.findByNome(nome);
	}
	
	//Caso d'uso: visualizzazione del dettaglio di una squadra (con giocatori)
	@Transactional(readOnly = true) 
	public Squadra findById(Long id) {
		return squadraRepository.findById(id).get();
	}

	// Caso d'uso: inserimento e modifica + eliminazione di una squadra

	@Transactional(rollbackFor = Exception.class)
    public void saveSquadra(Squadra squadra, MultipartFile fileLogo) throws IOException {
        
        if (fileLogo != null && !fileLogo.isEmpty()) {
            //conversione in byte
            squadra.setLogo(fileLogo.getBytes()); 
        } else if (squadra.getId() != null) {
            // se sto modificando e l'admin non ha messo un nuovo file,
            // recupero dal database il vecchio logo
            Squadra vecchiaSquadra = this.squadraRepository.findById(squadra.getId()).orElse(null);
            if (vecchiaSquadra != null) {
                squadra.setLogo(vecchiaSquadra.getLogo());
            }
        }
        
        
        this.squadraRepository.save(squadra);
    }


	@Transactional
	public void deleteSquadra(Long id) {
		Squadra squadra = this.squadraRepository.findById(id).orElse(null);
		if (squadra == null) return;

		//elimino le partecipazioni ai tornei relative a questa squadra
		if (squadra.getPartecipazioni() != null) {
			this.partecipazioneRepository.deleteAll(squadra.getPartecipazioni());
		}

		//elimino partite in cui questa squadra ha giocato in casa
		if (squadra.getPartiteInCasa() != null) {
			this.partitaRepository.deleteAll(squadra.getPartiteInCasa());
		}

		//elimino  partite in cui questa squadra ha giocato in trasferta
		if (squadra.getPartiteInTrasferta() != null) {
			this.partitaRepository.deleteAll(squadra.getPartiteInTrasferta());
		}

		//i giocatori rimangono "svincolati" senza squadra
		if (squadra.getGiocatori() != null) {
			for (Giocatore g : squadra.getGiocatori()) {
				g.setSquadra(null); 
			}
		}

		//elimina la squadra
		this.squadraRepository.delete(squadra);
	}
    
    @Transactional(readOnly = true)
    public boolean existsByNome(String nome) {
		return squadraRepository.existsByNome(nome);
	}
	
    
	@Transactional(readOnly = true)
	public void testLazy(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("LAZY");
		Squadra s = squadraRepository.findById(id).get();
		s.getGiocatori().size(); // forza il lazy loading
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	@Transactional(readOnly = true)
	public void testEager(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("EAGER");
		Squadra s = squadraRepository.findById(id).get();
		s.getGiocatori().size();
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	@Transactional(readOnly = true)
	public void testJoinFetch(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("JOIN FETCH");
		Squadra s = squadraRepository.findByIdWithGiocatori(id);
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	@Transactional(readOnly = true)
	public void testEntityGraph(Long id) {
		StopWatch sw = new StopWatch();
		sw.start("ENTITY GRAPH");
		Squadra s = squadraRepository.findByIdEntityGraph(id);
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	


}
