package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Arbitro;

public interface ArbitroRepository extends CrudRepository<Arbitro, Long>{
	
	public boolean existsByNomeAndCognome(String nome, String cognome);	

}
