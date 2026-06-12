package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;

public interface PartecipazioneRepository extends CrudRepository<Partecipazione, Long> {

    public Partecipazione findBySquadraAndTorneo(Squadra squadra, Torneo torneo);

    @Query(value="SELECT s.* "
            + "FROM partecipazione p "
            + "JOIN squadra s ON p.squadra_id = s.id "
            + "WHERE p.torneo_id = :torneoId "
            + "ORDER BY s.nome ASC", nativeQuery=true)
    public List<Squadra> findSquadreInTorneo(@Param("torneoId") Long id);
    
    @Query(value="SELECT * "
            + "FROM partecipazione p "
            + "WHERE p.torneo_id = :torneoId "
            + "ORDER BY p.punti DESC", nativeQuery=true)
    public List<Partecipazione> findClassificaByTorneoId(@Param("torneoId") Long id);
}