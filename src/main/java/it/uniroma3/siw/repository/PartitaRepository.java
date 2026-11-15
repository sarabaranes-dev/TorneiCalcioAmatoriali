package it.uniroma3.siw.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;

public interface PartitaRepository extends CrudRepository<Partita, Long>{
	public List<Partita> findByDataEora(LocalDateTime dataEora);

	// partite di una squadra (in casa o in trasferta) in ordine di data
	public List<Partita> findBySquadraCasaOrSquadraOspiteOrderByDataEoraAsc(Squadra squadraCasa, Squadra squadraOspite);

	public boolean existsByLuogoAndDataEora(String luogo, LocalDateTime dataEora);	
	
	public boolean existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(Torneo torneo, Squadra squadraCasa, Squadra squadraOspite, LocalDateTime dataEora);
}
