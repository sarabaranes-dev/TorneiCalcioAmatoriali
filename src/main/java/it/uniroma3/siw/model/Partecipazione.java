package it.uniroma3.siw.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Partecipazione {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private Integer punti=0;
	
	@ManyToOne
	private Torneo torneo;
	
	@ManyToOne
	private Squadra squadra;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPunti() {
		return punti;
	}

	public void setPunti(Integer punti) {
		this.punti = punti;
	}

	public Torneo getTorneo() {
		return torneo;
	}

	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}

	public Squadra getSquadra() {
		return squadra;
	}

	public void setSquadra(Squadra squadra) {
		this.squadra = squadra;
	}

	@Override
	public int hashCode() {
		return Objects.hash(squadra, torneo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partecipazione other = (Partecipazione) obj;
		return Objects.equals(squadra, other.squadra) && Objects.equals(torneo, other.torneo);
	}
	
	public void setVittoria() {
	    this.punti += 3;
	}

	public void setPareggio() {
	    this.punti += 1;
	}
	
	
	
	
}
