package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Torneo;

public interface TorneoRepository extends CrudRepository<Torneo, Long>{
	public List<Torneo> findByAnno(int anno);

	public boolean existsByNomeAndAnno(String nome, int anno);	

	@Query("SELECT t FROM Torneo t " +
           "LEFT JOIN FETCH t.partite p " +
           "LEFT JOIN FETCH p.squadraCasa " +
           "LEFT JOIN FETCH p.squadraOspite " +
           "WHERE t.id = :id")
    Optional<Torneo> findByIdWithPartiteAndSquadre(@Param("id") Long id);
}
