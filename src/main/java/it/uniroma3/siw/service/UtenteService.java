package it.uniroma3.siw.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtenteService {

    @Autowired
    protected UtenteRepository utenteRepository;

    @Transactional(readOnly = true)
    public Utente getUtente(Long id) {
        Optional<Utente> risultato = this.utenteRepository.findById(id);
        return risultato.orElse(null);
    }

    
    @Transactional
    public Utente saveUtente(Utente utente) {
        return this.utenteRepository.save(utente);
    }

    @Transactional(readOnly = true)
    public List<Utente> getAllUtenti() {
        List<Utente> risultato = new ArrayList<>();
        Iterable<Utente> iterable = this.utenteRepository.findAll();
        for(Utente utente : iterable)
            risultato.add(utente);
        return risultato;
    }
}
