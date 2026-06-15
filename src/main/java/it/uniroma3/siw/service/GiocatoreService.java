package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.repository.GiocatoreRepository;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.domain.Pageable;

@Service
public class GiocatoreService {

    @Autowired
    private GiocatoreRepository giocatoreRepository;
    
    @Transactional(readOnly = true)
    public List<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    // Caso d'uso: inserimento e modifica + eliminazione di un giocatore
    @Transactional
    public void saveGiocatore(Giocatore giocatore) {
        this.giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteGiocatore(Long id) {
        this.giocatoreRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).get();
    }

    public boolean existsByNomeAndCognomeAndDataNascita(String nome, String cognome, LocalDate data) {
        return giocatoreRepository.existsByNomeAndCognomeAndDataNascita(nome, cognome, data);
    }
    
    @Transactional(readOnly = true)
    public Page<Giocatore> getGiocatoriCercatiEPaginati(String keyword, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize); // Spring conta le pagine da 0
        
        if (keyword != null && !keyword.isEmpty()) {
            return giocatoreRepository.findByCognomeContainingIgnoreCase(keyword, pageable);
        }
        return giocatoreRepository.findAll(pageable);
    }

}