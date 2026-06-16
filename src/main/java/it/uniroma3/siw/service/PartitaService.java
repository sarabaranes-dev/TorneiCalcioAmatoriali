package it.uniroma3.siw.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import it.uniroma3.siw.model.Partecipazione;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.PartecipazioneRepository;
import it.uniroma3.siw.repository.PartitaRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private PartecipazioneRepository partecipazioneRepository;

    // Caso d'uso: visualizzazione del calendario delle partite
    @Transactional(readOnly = true)
    public Iterable<Partita> findAll() {
        return partitaRepository.findAll();
    }
    

    // Caso d'uso: inserimento o modifica del risultato di una partita e ricalcolo totale della classifica
    /**
     * lettura dello stato globale del torneo, 
     * ricalcolo matematico dei punteggi e aggiornamento di più entità correlate.
     * isolamento massimo per evitare disallineamenti della classifica in caso di concorrrenza.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggiornaRisultato(Long idPartita, int goalsCasa, int goalsOspite) {
        Partita partita = this.partitaRepository.findById(idPartita).orElse(null);
        if (partita == null) return;

        //aggiorno goal e stato della partita corrente
        partita.setGoalsHome(goalsCasa);
        partita.setGoalsAway(goalsOspite);
        partita.setStato(Partita.StatoPartita.PLAYED);
        this.partitaRepository.save(partita);

        Torneo torneo = partita.getTorneo();
        if (torneo == null) return;

        //recupero le partecipazioni delle due squadre per questo torneo
        Partecipazione partCasa = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraCasa(), torneo);
        Partecipazione partOspite = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraOspite(), torneo);

        // se le partecipazioni non esistevano le creo
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

        //scorro tutte le partite del torneo per calcolare i punti reali
        int puntiCasa = 0;
        int puntiOspite = 0;

        if (torneo.getPartite() != null) {
            for (Partita p : torneo.getPartite()) { 
                //considero solo i match effettivamente giocati
                if (p.getStato() != null && p.getStato().name().equals("PLAYED")) {
                    
                    //controllo i match della squadra di casa
                    if (p.getSquadraCasa() != null && p.getSquadraCasa().equals(partita.getSquadraCasa())) {
                        if (p.getGoalsHome() > p.getGoalsAway()) puntiCasa += 3;
                        else if (p.getGoalsHome() == p.getGoalsAway()) puntiCasa += 1;
                    } else if (p.getSquadraOspite() != null && p.getSquadraOspite().equals(partita.getSquadraCasa())) {
                        if (p.getGoalsAway() > p.getGoalsHome()) puntiCasa += 3;
                        else if (p.getGoalsAway() == p.getGoalsHome()) puntiCasa += 1;
                    }

                    //controllo i match della squadra ospite
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

        // salvo i punti aggiornati nel database
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
        //salvo regolarmente la partita nel database
        this.partitaRepository.save(partita);

        if (partita.getTorneo() != null) {
            //controllo se la squadra di casa è già iscritta a questo torneo
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

            //stesso coontrollo per squadra ospite
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deletePartita(Long id) {
        Partita partita = this.partitaRepository.findById(id).orElse(null);
        if (partita == null) return;

        Torneo torneo = partita.getTorneo();
        
        //rimuovo riferimento dalla lista in memoria per sicurezza
        if (torneo != null && torneo.getPartite() != null) {
            torneo.getPartite().remove(partita);
        }

        // AGGIORNAMENTO CLASSIFICA: Ricalcoliamo solo se la partita era stata effettivamente giocata
        if (partita.getStato() != null && partita.getStato() == Partita.StatoPartita.PLAYED && torneo != null) {
            
            Partecipazione partCasa = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraCasa(), torneo);
            Partecipazione partOspite = this.partecipazioneRepository.findBySquadraAndTorneo(partita.getSquadraOspite(), torneo);

            int puntiCasa = 0;
            int puntiOspite = 0;

            // scansiono partite rimanenti del torneo
            if (torneo.getPartite() != null) {
                for (Partita p : torneo.getPartite()) {
                   
                    if (p.getId().equals(partita.getId())) continue;

                    if (p.getStato() != null && p.getStato() == Partita.StatoPartita.PLAYED) {
                        
                        //controllo match della squadra di casa
                        if (p.getSquadraCasa() != null && p.getSquadraCasa().equals(partita.getSquadraCasa())) {
                            if (p.getGoalsHome() > p.getGoalsAway()) puntiCasa += 3;
                            else if (p.getGoalsHome() == p.getGoalsAway()) puntiCasa += 1;
                        } else if (p.getSquadraOspite() != null && p.getSquadraOspite().equals(partita.getSquadraCasa())) {
                            if (p.getGoalsAway() > p.getGoalsHome()) puntiCasa += 3;
                            else if (p.getGoalsAway() == p.getGoalsHome()) puntiCasa += 1;
                        }

                        //controllo match della squadra ospite
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

            if (partCasa != null) {
                partCasa.setPunti(puntiCasa);
                this.partecipazioneRepository.save(partCasa);
            }
            if (partOspite != null) {
                partOspite.setPunti(puntiOspite);
                this.partecipazioneRepository.save(partOspite);
            }
        }

        this.partitaRepository.delete(partita);
    }
        
    // Caso d'uso: recupero di una singola partita
    @Transactional(readOnly = true)
    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElse(null);
    }

    //controlla se una partita identica esiste già
    public boolean existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(Torneo torneo, Squadra squadraCasa, Squadra squadraOspite, LocalDateTime dataEora) {
        return partitaRepository.existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(torneo, squadraCasa, squadraOspite, dataEora);
    }
}