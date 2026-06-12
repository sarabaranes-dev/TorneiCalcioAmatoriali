package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Utente;

public interface CommentoRepository extends CrudRepository<Commento, Long> {
	
	//Per visualizzare i commenti di una specifica partita
    public List<Commento> findByPartitaCommentataId(Long id);

    //Per visualizzare i commenti  di un autore specifico
    public List<Commento> findByAutore(Utente autore);
    
    //visualizza tutti i commenti di una partita
    @Query(value="select * "
			+ "from commento c "
			+ "where c.partita_commentata_id = :partitaId "
			+ "order by c.data ASC", nativeQuery=true)    
    public List<Commento> findCommentiInPartita(@Param("partitaId") Long id);
}
