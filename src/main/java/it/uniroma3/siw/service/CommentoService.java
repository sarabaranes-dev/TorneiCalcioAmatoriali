package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CommentoRepository;
import jakarta.transaction.Transactional;

@Service
public class CommentoService {

	@Autowired
	private CommentoRepository commentoRepository;

	
	//Caso d'uso: visualizzazione commenti
	public Iterable<Commento> findAll() {
		return commentoRepository.findAll();
	}
	
	//Caso d'uso:inserimento commento
	@Transactional
	public void inserisciCommento(Commento commento, Utente autore) {
		commento.setAutore(autore);
		commento.setData(LocalDateTime.now());
		this.commentoRepository.save(commento);
	}
	
	//Caso d'uso:modifica proprio commento
	@Transactional
    public void modificaCommento(Long idCommento, String nuovoTesto, Utente user) {
        Commento commento = this.commentoRepository.findById(idCommento).orElse(null);
        
        if (commento != null && commento.getAutore() != null && user != null
                && commento.getAutore().getEmail() != null
                && commento.getAutore().getEmail().equals(user.getEmail())) {
            
            commento.setTesto(nuovoTesto);
            
            this.commentoRepository.save(commento);
        }
    }
	@Transactional 
	public Commento findById(Long id) {
		return commentoRepository.findById(id).orElse(null);
	}
	

	@Transactional 
	public List<Commento> findByPartitaCommentataId(Long partitaId) {
		return this.commentoRepository.findCommentiInPartita(partitaId);
	}
	
	    
	
}
