package it.uniroma3.siw.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Squadra {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;

	private Integer annoFondazione;
	private String nome;
	private String citta;
	
	@Lob
    private byte[] logo;
	
	
	@OneToMany(mappedBy="squadra")
	private List<Giocatore> giocatori;
	
	
	@OneToMany(mappedBy = "squadraCasa")
	@JsonIgnore
    private List<Partita> partiteInCasa;

	@OneToMany(mappedBy = "squadraOspite")
	@JsonIgnore
    private List<Partita> partiteInTrasferta;

	@OneToMany(mappedBy= "squadra")
	@JsonIgnore
	private List<Partecipazione> partecipazioni;
	
	
	public Squadra() {
		this.giocatori=new LinkedList<Giocatore>();
		this.partiteInTrasferta=new LinkedList<Partita>();
		this.partiteInCasa=new LinkedList<Partita>();
		this.partecipazioni=new LinkedList<Partecipazione>();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAnnoFondazione() {
		return annoFondazione;
	}
	public void setAnnoFondazione(Integer annoFondazione) {
		this.annoFondazione = annoFondazione;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	
	
	public List<Giocatore> getGiocatori() {
		return giocatori;
	}


	public void setGiocatori(List<Giocatore> giocatori) {
		this.giocatori = giocatori;
	}


	public List<Partita> getPartiteInCasa() {
		return partiteInCasa;
	}


	public void setPartiteInCasa(List<Partita> partiteInCasa) {
		this.partiteInCasa = partiteInCasa;
	}


	public List<Partita> getPartiteInTrasferta() {
		return partiteInTrasferta;
	}


	public void setPartiteInTrasferta(List<Partita> partiteInTrasferta) {
		this.partiteInTrasferta = partiteInTrasferta;
	}


	public List<Partecipazione> getPartecipazioni() {
		return partecipazioni;
	}


	public void setPartecipazioni(List<Partecipazione> tornei) {
		this.partecipazioni = tornei;
	}

	public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

	

	@Override
	public int hashCode() {
		return Objects.hash(annoFondazione, nome);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Squadra other = (Squadra) obj;
		return Objects.equals(annoFondazione, other.annoFondazione) && Objects.equals(nome, other.nome);
	}
	
		
}
