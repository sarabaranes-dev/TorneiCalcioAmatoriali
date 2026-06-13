package it.uniroma3.siw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.uniroma3.siw.service.SquadraService;

@SpringBootApplication
public class SiwTorneiCalcioApplication implements CommandLineRunner{

	
    @Autowired
    private SquadraService squadraService;

	public static void main(String[] args) {
		SpringApplication.run(SiwTorneiCalcioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Long id = 1L;

		squadraService.testLazy(id);
		squadraService.testJoinFetch(id);
		squadraService.testEntityGraph(id);
	}

}

/* caso d'uso: caricare una squadra e i suoi giocatori.
1. LAZY: carica la squadra, ma non i giocatori.  
   StopWatch: 043850375 ns  (≈ 43 ms)

2. JOIN FETCH: carica la squadra e i giocatori in un'unica query, utilizzando un JOIN,
   più efficiente perché riduce il numero di query al database. 
   StopWatch: 020707333 ns  (≈ 20 ms)


3. ENTITY GRAPH: simile a JOIN FETCH, ma più flessibile. definire un "graph" specifica quali relazioni caricare.
   query ottimizzata con un left join
   StopWatch: 007926041 ns  (≈ 7 ms)
*/