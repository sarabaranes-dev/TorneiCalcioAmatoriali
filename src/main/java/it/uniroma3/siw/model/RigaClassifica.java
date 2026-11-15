package it.uniroma3.siw.model;

/**
 * Riga della classifica di un torneo: punti presi dalla Partecipazione,
 * statistiche (giocate, vinte, pari, perse, gol) calcolate dalle partite con un risultato.
 * Non e' un'entita' JPA: viene costruita dal TorneoService.
 */
public class RigaClassifica {

    private final Squadra squadra;
    private final int punti;
    private int giocate;
    private int vinte;
    private int pareggiate;
    private int perse;
    private int golFatti;
    private int golSubiti;

    public RigaClassifica(Squadra squadra, int punti) {
        this.squadra = squadra;
        this.punti = punti;
    }

    /** Aggiunge una partita giocata dal punto di vista di questa squadra. */
    public void aggiungiRisultato(int golSegnati, int golConcessi) {
        this.giocate++;
        this.golFatti += golSegnati;
        this.golSubiti += golConcessi;
        if (golSegnati > golConcessi) this.vinte++;
        else if (golSegnati == golConcessi) this.pareggiate++;
        else this.perse++;
    }

    public Squadra getSquadra() { return squadra; }
    public int getPunti() { return punti; }
    public int getGiocate() { return giocate; }
    public int getVinte() { return vinte; }
    public int getPareggiate() { return pareggiate; }
    public int getPerse() { return perse; }
    public int getGolFatti() { return golFatti; }
    public int getGolSubiti() { return golSubiti; }
    public int getDifferenzaReti() { return golFatti - golSubiti; }
}
