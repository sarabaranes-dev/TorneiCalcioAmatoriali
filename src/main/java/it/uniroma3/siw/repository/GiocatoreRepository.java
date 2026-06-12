package it.uniroma3.siw.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository; 

import it.uniroma3.siw.model.Giocatore;

// Estende JpaRepository anziché CrudRepository
public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {
    
    public boolean existsByNomeAndCognome(String nome, String cognome); 

    public boolean existsByNomeAndCognomeAndDataNascita(String nome, String cognome, LocalDate data);   

    Page<Giocatore> findByCognomeContainingIgnoreCase(String cognome, Pageable pageable);
    
}