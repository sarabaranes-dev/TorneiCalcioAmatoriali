package it.uniroma3.siw.benchmark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;

/**
 * Confronto dei tempi di caricamento di una squadra con i suoi giocatori
 * (LAZY, EAGER, JOIN FETCH, ENTITY GRAPH).
 * Gira solo con il profilo "benchmark":
 *   ./mvnw spring-boot:run -Dspring-boot.run.profiles=benchmark
 */
@Component
@Profile("benchmark")
public class FetchStrategyBenchmark implements CommandLineRunner {

    @Autowired
    private SquadraService squadraService;

    @Override
    public void run(String... args) {
        // usa la prima squadra presente nel database
        Squadra squadra = null;
        for (Squadra s : squadraService.findAll()) {
            squadra = s;
            break;
        }
        if (squadra == null) {
            System.out.println("Benchmark saltato: nessuna squadra nel database.");
            return;
        }
        Long id = squadra.getId();
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
