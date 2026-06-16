package it.uniroma3.siw.model; 

public class PartitaDTO {
    private Long id;
    private String luogo;
    private Integer goalsHome;
    private Integer goalsAway;
    private String dataEora;
    private String stato;
    
    //invece di stringhe, uso dei mini-oggetti "finti" per React
    private SottoOggettoDTO torneo;
    private SottoOggettoDTO squadraCasa; 
    private SottoOggettoDTO squadraOspite;

    public PartitaDTO(Partita partita) {
        this.id = partita.getId();
        this.luogo = partita.getLuogo();
        this.goalsHome = partita.getGoalsHome();
        this.goalsAway = partita.getGoalsAway();
        this.dataEora = partita.getDataEora() != null ? partita.getDataEora().toString() : null;
        this.stato = partita.getStato() != null ? partita.getStato().toString() : null;
        
      
        if (partita.getTorneo() != null) {
            this.torneo = new SottoOggettoDTO(partita.getTorneo().getId(), partita.getTorneo().getNome());
        }
        if (partita.getSquadraCasa() != null) {
            this.squadraCasa = new SottoOggettoDTO(partita.getSquadraCasa().getId(), partita.getSquadraCasa().getNome());
        }
        if (partita.getSquadraOspite() != null) {
            this.squadraOspite = new SottoOggettoDTO(partita.getSquadraOspite().getId(), partita.getSquadraOspite().getNome());
        }
    }

    // --- CLASSE INTERNA DI APPOGGIO ---
    public static class SottoOggettoDTO {
        private Long id;
        private String nome;

        public SottoOggettoDTO(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    public Long getId() {
        return id;
    }

    public String getLuogo() {
        return luogo;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public String getDataEora() {
        return dataEora;
    }

    public String getStato() {
        return stato;
    }

    public SottoOggettoDTO getTorneo() {
        return torneo;
    }

    public SottoOggettoDTO getSquadraCasa() {
        return squadraCasa;
    }

    public SottoOggettoDTO getSquadraOspite() {
        return squadraOspite;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public void setDataEora(String dataEora) {
        this.dataEora = dataEora;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setTorneo(SottoOggettoDTO torneo) {
        this.torneo = torneo;
    }

    public void setSquadraCasa(SottoOggettoDTO squadraCasa) {
        this.squadraCasa = squadraCasa;
    }

    public void setSquadraOspite(SottoOggettoDTO squadraOspite) {
        this.squadraOspite = squadraOspite;
    }

    
}