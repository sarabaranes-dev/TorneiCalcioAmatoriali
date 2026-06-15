package it.uniroma3.siw.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.PartitaRepository;
import jakarta.transaction.Transactional;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private PartecipazioneRepository partecipazioneRepository;

    // Caso d'uso: visualizzazione del calendario delle partite
    public Iterable<Partita> findAll() {
        return partitaRepository.findAll();
    }
    
    // Caso d'uso: inserimento del risultato di una partita e aggiornamento classifica
    // Caso d'uso: inserimento/modifica del risultato di una partita e ricalcolo totale della classifica
    @Transactional
    public void aggiornaRisultato(Long idPartita, int goalsCasa, int goalsOspite) {
        Partita partita = this.partitaRepository.findById(idPartita).orElse(null);
        if (partita == null) return;

        // 1. Aggiorno i gol e lo stato della partita corrente
        partita.setGoalsHome(goalsCasa);
        partita.setGoalsAway(goalsOspite);
        partita.setStato(Partita.StatoPartita.PLAYED);
        this.partitaRepository.save(partita);

        Torneo torneo = partita.getTorneo();
        if (torneo == null) return;

        // 2. Recupero le partecipazioni delle due squadre per questo torneo
        Partecipazione partCasa = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraCasa(), torneo);
        Partecipazione partOspite = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraOspite(), torneo);

        // Se le partecipazioni non esistevano (es. partite vecchie nate senza iscrizione), le creiamo al volo
        if (partCasa == null && partita.getSquadraCasa() != null) {
            partCasa = new Partecipazione();
            partCasa.setTorneo(torneo);
            partCasa.setSquadra(partita.getSquadraCasa());
            partCasa.setPunti(0);
            this.partecipazioneRepository.save(partCasa);
        }
        if (partOspite == null && partita.getSquadraOspite() != null) {
            partOspite = new Partecipazione();
            partOspite.setTorneo(torneo);
            partOspite.setSquadra(partita.getSquadraOspite());
            partOspite.setPunti(0);
            this.partecipazioneRepository.save(partOspite);
        }

        // 3. RICALCOLO TOTALE: Scorriamo tutte le partite del torneo per calcolare i punti reali
        int puntiCasa = 0;
        int puntiOspite = 0;

        if (torneo.getPartite() != null) {
            for (Partita p : torneo.getPartite()) { 
                // Consideriamo solo i match effettivamente giocati
                if (p.getStato() != null && p.getStato().name().equals("PLAYED")) {
                    
                    // Controllo i match della Squadra di Casa corrente
                    if (p.getSquadraCasa() != null && p.getSquadraCasa().equals(partita.getSquadraCasa())) {
                        if (p.getGoalsHome() > p.getGoalsAway()) puntiCasa += 3;
                        else if (p.getGoalsHome() == p.getGoalsAway()) puntiCasa += 1;
                    } else if (p.getSquadraOspite() != null && p.getSquadraOspite().equals(partita.getSquadraCasa())) {
                        if (p.getGoalsAway() > p.getGoalsHome()) puntiCasa += 3;
                        else if (p.getGoalsAway() == p.getGoalsHome()) puntiCasa += 1;
                    }

                    // Controllo i match della Squadra Ospite corrente
                    if (p.getSquadraCasa() != null && p.getSquadraCasa().equals(partita.getSquadraOspite())) {
                        if (p.getGoalsHome() > p.getGoalsAway()) puntiOspite += 3;
                        else if (p.getGoalsHome() == p.getGoalsAway()) puntiOspite += 1;
                    } else if (p.getSquadraOspite() != null && p.getSquadraOspite().equals(partita.getSquadraOspite())) {
                        if (p.getGoalsAway() > p.getGoalsHome()) puntiOspite += 3;
                        else if (p.getGoalsAway() == p.getGoalsHome()) puntiOspite += 1;
                    }
                }
            }
        }

        // 4. Salvo i punti aggiornati e definitivi nel database
        if (partCasa != null) {
            partCasa.setPunti(puntiCasa);
            this.partecipazioneRepository.save(partCasa);
        }
        if (partOspite != null) {
            partOspite.setPunti(puntiOspite);
            this.partecipazioneRepository.save(partOspite);
        }
    }

    // Caso d'uso: salvataggio di una partita con iscrizione automatica di sicurezza delle squadre
    @Transactional
    public void savePartita(Partita partita) {
        // Salvo regolarmente la partita nel database
        this.partitaRepository.save(partita);

        if (partita.getTorneo() != null) {
            // Controllo se la Squadra di Casa è già iscritta a questo torneo
            if (partita.getSquadraCasa() != null) {
                Partecipazione partCasa = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraCasa(), partita.getTorneo());
                if (partCasa == null) {
                    Partecipazione nuovaPartecipazione = new Partecipazione();
                    nuovaPartecipazione.setTorneo(partita.getTorneo());
                    nuovaPartecipazione.setSquadra(partita.getSquadraCasa());
                    nuovaPartecipazione.setPunti(0);
                    this.partecipazioneRepository.save(nuovaPartecipazione);
                }
            }

            // Controllo se la Squadra Ospite è già iscritta a questo torneo
            if (partita.getSquadraOspite() != null) {
                Partecipazione partOspite = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraOspite(), partita.getTorneo());
                if (partOspite == null) {
                    Partecipazione nuovaPartecipazione = new Partecipazione();
                    nuovaPartecipazione.setTorneo(partita.getTorneo());
                    nuovaPartecipazione.setSquadra(partita.getSquadraOspite());
                    nuovaPartecipazione.setPunti(0);
                    this.partecipazioneRepository.save(nuovaPartecipazione);
                }
            }
        }
    }

    // Caso d'uso: eliminazione di una partita
    @Transactional
    public void deletePartita(Long id) {
        this.partitaRepository.deleteById(id);
    }
    
    // Caso d'uso: recupero di una singola partita tramite ID
    @Transactional
    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElse(null);
    }

    // Validazione: controlla se una partita identica esiste già
    public boolean existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(Torneo torneo, Squadra squadraCasa, Squadra squadraOspite, LocalDateTime dataEora) {
        return partitaRepository.existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(torneo, squadraCasa, squadraOspite, dataEora);
    }
}