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
		squadraService.testEager(id);
		squadraService.testJoinFetch(id);
		squadraService.testEntityGraph(id);
	}

}

/* caso d'uso: caricare una squadra e i suoi giocatori.
1. LAZY: carica la squadra, ma non i giocatori.  

2. EAGER: carica immediatamente la collezione dei giocatori quando viene caricata la squadra.
   il tempo di esecuzione è simile a quello della strategia LAZY solitamente,
   ma carica dati anche quando non necessari e
   può generare join molto pesanti
   
3. JOIN FETCH: carica la squadra e i giocatori in un'unica query, utilizzando un JOIN,
   più efficiente perché riduce il numero di query al database. 

4. ENTITY GRAPH: simile a JOIN FETCH, ma più flessibile. definire un "graph" specifica quali relazioni caricare.
   query ottimizzata con un left join
*/