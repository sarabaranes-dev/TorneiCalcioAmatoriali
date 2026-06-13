package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Squadra;

public interface SquadraRepository extends CrudRepository<Squadra, Long>{
	public List<Squadra> findByAnnoFondazione(int annoFondazione);

	public boolean existsByNomeAndAnnoFondazione(String nome, int annoFondazione); 	
	
	public boolean existsByNome(String nome);

	public Optional<Squadra> findByNome(String nome);

	@Query("SELECT s FROM Squadra s JOIN FETCH s.giocatori WHERE s.id = :id")
	Squadra findByIdWithGiocatori(Long id);

	@EntityGraph(attributePaths = {"giocatori"})
	@Query("SELECT s FROM Squadra s WHERE s.id = :id")
	Squadra findByIdEntityGraph(Long id);

}
