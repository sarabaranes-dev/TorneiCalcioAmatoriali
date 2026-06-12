package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Torneo;

public interface TorneoRepository extends CrudRepository<Torneo, Long>{
	public List<Torneo> findByAnno(int anno);

	public boolean existsByNomeAndAnno(String nome, int anno);	
}
