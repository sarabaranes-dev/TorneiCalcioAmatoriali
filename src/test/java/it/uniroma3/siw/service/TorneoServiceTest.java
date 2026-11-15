package it.uniroma3.siw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Partita.StatoPartita;
import it.uniroma3.siw.model.RigaClassifica;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TorneoRepository;

/** Unit tests for the detailed standings (no database needed). */
@ExtendWith(MockitoExtension.class)
class TorneoServiceTest {

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private PartecipazioneRepository partecipazioneRepository;

    @Mock
    private SquadraRepository squadraRepository;

    @InjectMocks
    private TorneoService torneoService;

    private static Squadra squadra(String nome) {
        Squadra s = new Squadra();
        s.setNome(nome);
        s.setAnnoFondazione(2020);
        return s;
    }

    private static Partecipazione partecipazione(Torneo torneo, Squadra squadra, int punti) {
        Partecipazione p = new Partecipazione();
        p.setTorneo(torneo);
        p.setSquadra(squadra);
        p.setPunti(punti);
        return p;
    }

    private static Partita partita(Squadra casa, Squadra ospite, Integer gc, Integer go, StatoPartita stato) {
        Partita p = new Partita();
        p.setSquadraCasa(casa);
        p.setSquadraOspite(ospite);
        p.setGoalsHome(gc);
        p.setGoalsAway(go);
        p.setStato(stato);
        return p;
    }

    @Test
    void getClassificaDettagliata_countsResultsAndBreaksTiesOnGoalDifference() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        Squadra c = squadra("C");
        // A beats C 1-0, B beats C 3-0, A vs B still to play: A and B both have 3 points
        torneo.setPartite(new ArrayList<>(List.of(
                partita(a, c, 1, 0, StatoPartita.PLAYED),
                partita(c, b, 0, 3, StatoPartita.PLAYED),
                partita(a, b, null, null, StatoPartita.SCHEDULED))));
        when(partecipazioneRepository.findClassificaByTorneoId(1L)).thenReturn(List.of(
                partecipazione(torneo, a, 3), partecipazione(torneo, b, 3), partecipazione(torneo, c, 0)));
        when(torneoRepository.findByIdWithPartiteAndSquadre(1L)).thenReturn(Optional.of(torneo));

        List<RigaClassifica> classifica = torneoService.getClassificaDettagliata(1L);

        assertEquals("B", classifica.get(0).getSquadra().getNome()); // +3 goal difference
        assertEquals("A", classifica.get(1).getSquadra().getNome()); // +1
        assertEquals("C", classifica.get(2).getSquadra().getNome());
        RigaClassifica rigaC = classifica.get(2);
        assertEquals(2, rigaC.getGiocate());
        assertEquals(0, rigaC.getVinte());
        assertEquals(2, rigaC.getPerse());
        assertEquals(0, rigaC.getGolFatti());
        assertEquals(4, rigaC.getGolSubiti());
        assertEquals(-4, rigaC.getDifferenzaReti());
        assertEquals(1, classifica.get(1).getGiocate()); // the scheduled match is not counted
    }
}
