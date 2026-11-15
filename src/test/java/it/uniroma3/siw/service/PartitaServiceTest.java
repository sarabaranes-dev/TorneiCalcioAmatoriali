package it.uniroma3.siw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Partita.StatoPartita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.PartitaRepository;

/** Unit tests for the match use cases and the standings update (no database needed). */
@ExtendWith(MockitoExtension.class)
class PartitaServiceTest {

    @Mock
    private PartitaRepository partitaRepository;

    @Mock
    private PartecipazioneRepository partecipazioneRepository;

    @InjectMocks
    private PartitaService partitaService;

    private static Squadra squadra(String nome) {
        Squadra s = new Squadra();
        s.setNome(nome);
        s.setAnnoFondazione(2020);
        return s;
    }

    private static Partita partita(long id, Torneo torneo, Squadra casa, Squadra ospite,
                                   Integer goalsCasa, Integer goalsOspite, StatoPartita stato) {
        Partita p = new Partita();
        p.setId(id);
        p.setTorneo(torneo);
        p.setSquadraCasa(casa);
        p.setSquadraOspite(ospite);
        p.setGoalsHome(goalsCasa);
        p.setGoalsAway(goalsOspite);
        p.setStato(stato);
        return p;
    }

    private static Partecipazione partecipazione(Torneo torneo, Squadra squadra, int punti) {
        Partecipazione p = new Partecipazione();
        p.setTorneo(torneo);
        p.setSquadra(squadra);
        p.setPunti(punti);
        return p;
    }

    @Test
    void aggiornaRisultato_homeWin_marksMatchPlayedAndRecalculatesPoints() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        Squadra c = squadra("C");
        // A already drew with C, B already beat C
        Partita giocata1 = partita(1L, torneo, a, c, 1, 1, StatoPartita.PLAYED);
        Partita giocata2 = partita(2L, torneo, c, b, 0, 2, StatoPartita.PLAYED);
        Partita daGiocare = partita(3L, torneo, a, b, null, null, StatoPartita.SCHEDULED);
        torneo.setPartite(new ArrayList<>(List.of(giocata1, giocata2, daGiocare)));

        Partecipazione partA = partecipazione(torneo, a, 1);
        Partecipazione partB = partecipazione(torneo, b, 3);
        when(partitaRepository.findById(3L)).thenReturn(Optional.of(daGiocare));
        when(partecipazioneRepository.findBySquadraAndTorneo(a, torneo)).thenReturn(partA);
        when(partecipazioneRepository.findBySquadraAndTorneo(b, torneo)).thenReturn(partB);

        partitaService.aggiornaRisultato(3L, 2, 0);

        assertEquals(StatoPartita.PLAYED, daGiocare.getStato());
        assertEquals(2, daGiocare.getGoalsHome());
        assertEquals(0, daGiocare.getGoalsAway());
        assertEquals(4, partA.getPunti()); // draw (1) + win (3)
        assertEquals(3, partB.getPunti()); // win (3) + loss (0)
    }

    @Test
    void aggiornaRisultato_correctingAResult_doesNotCountTheMatchTwice() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        // the match was saved as 1-0, the admin corrects it to 1-1
        Partita giocata = partita(1L, torneo, a, b, 1, 0, StatoPartita.PLAYED);
        torneo.setPartite(new ArrayList<>(List.of(giocata)));

        Partecipazione partA = partecipazione(torneo, a, 3);
        Partecipazione partB = partecipazione(torneo, b, 0);
        when(partitaRepository.findById(1L)).thenReturn(Optional.of(giocata));
        when(partecipazioneRepository.findBySquadraAndTorneo(a, torneo)).thenReturn(partA);
        when(partecipazioneRepository.findBySquadraAndTorneo(b, torneo)).thenReturn(partB);

        partitaService.aggiornaRisultato(1L, 1, 1);

        assertEquals(1, partA.getPunti());
        assertEquals(1, partB.getPunti());
    }

    @Test
    void aggiornaRisultato_ignoresPlayedMatchesWithoutAScore() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        // a match created with a past date is PLAYED but has no score yet
        Partita senzaRisultato = partita(1L, torneo, a, b, null, null, StatoPartita.PLAYED);
        Partita daAggiornare = partita(2L, torneo, b, a, null, null, StatoPartita.PLAYED);
        torneo.setPartite(new ArrayList<>(List.of(senzaRisultato, daAggiornare)));

        Partecipazione partA = partecipazione(torneo, a, 0);
        Partecipazione partB = partecipazione(torneo, b, 0);
        when(partitaRepository.findById(2L)).thenReturn(Optional.of(daAggiornare));
        when(partecipazioneRepository.findBySquadraAndTorneo(a, torneo)).thenReturn(partA);
        when(partecipazioneRepository.findBySquadraAndTorneo(b, torneo)).thenReturn(partB);

        partitaService.aggiornaRisultato(2L, 2, 1);

        assertEquals(0, partA.getPunti());
        assertEquals(3, partB.getPunti());
    }

    @Test
    void aggiornaRisultato_unknownMatch_doesNothing() {
        when(partitaRepository.findById(99L)).thenReturn(Optional.empty());

        partitaService.aggiornaRisultato(99L, 1, 0);

        verify(partitaRepository, never()).save(any());
        verify(partecipazioneRepository, never()).save(any());
    }

    @Test
    void savePartita_futureDate_isScheduledAndEnrollsBothTeams() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        Partita nuova = partita(1L, torneo, a, b, null, null, null);
        nuova.setDataEora(LocalDateTime.now().plusDays(7));
        when(partecipazioneRepository.findBySquadraAndTorneo(any(), any())).thenReturn(null);

        partitaService.savePartita(nuova);

        assertEquals(StatoPartita.SCHEDULED, nuova.getStato());
        verify(partitaRepository).save(nuova);
        ArgumentCaptor<Partecipazione> captor = ArgumentCaptor.forClass(Partecipazione.class);
        verify(partecipazioneRepository, times(2)).save(captor.capture());
        List<Partecipazione> iscrizioni = captor.getAllValues();
        assertEquals(a, iscrizioni.get(0).getSquadra());
        assertEquals(b, iscrizioni.get(1).getSquadra());
        assertEquals(0, iscrizioni.get(0).getPunti());
    }

    @Test
    void savePartita_pastDate_isPlayedAndKeepsExistingEnrollments() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        Partita vecchia = partita(1L, torneo, a, b, 2, 1, null);
        vecchia.setDataEora(LocalDateTime.now().minusDays(7));
        when(partecipazioneRepository.findBySquadraAndTorneo(a, torneo)).thenReturn(partecipazione(torneo, a, 0));
        when(partecipazioneRepository.findBySquadraAndTorneo(b, torneo)).thenReturn(partecipazione(torneo, b, 0));

        partitaService.savePartita(vecchia);

        assertEquals(StatoPartita.PLAYED, vecchia.getStato());
        verify(partecipazioneRepository, never()).save(any());
    }

    @Test
    void deletePartita_playedMatch_removesItsPointsFromTheStandings() {
        Torneo torneo = new Torneo();
        Squadra a = squadra("A");
        Squadra b = squadra("B");
        Partita daEliminare = partita(1L, torneo, a, b, 3, 0, StatoPartita.PLAYED);
        Partita altra = partita(2L, torneo, b, a, 1, 1, StatoPartita.PLAYED);
        torneo.setPartite(new ArrayList<>(List.of(daEliminare, altra)));

        Partecipazione partA = partecipazione(torneo, a, 4);
        Partecipazione partB = partecipazione(torneo, b, 1);
        when(partitaRepository.findById(1L)).thenReturn(Optional.of(daEliminare));
        when(partecipazioneRepository.findBySquadraAndTorneo(a, torneo)).thenReturn(partA);
        when(partecipazioneRepository.findBySquadraAndTorneo(b, torneo)).thenReturn(partB);

        partitaService.deletePartita(1L);

        assertEquals(1, partA.getPunti()); // only the remaining draw
        assertEquals(1, partB.getPunti());
        verify(partitaRepository).delete(daEliminare);
    }
}
