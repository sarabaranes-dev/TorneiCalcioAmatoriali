package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Arbitro {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String nome;
	private String cognome;
	private String codice_arbitrale;

	 
	@OneToMany(mappedBy = "arbitro")
	@JsonIgnore
    private List<Partita> partiteDirette;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id=id;
	}
	
	public String getCodice_arbitrale() {
		return codice_arbitrale;
	}
	public void setCodice_arbitrale(String codice_arbitrale) {
		this.codice_arbitrale = codice_arbitrale;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public List<Partita> getPartiteDirette(){
		return partiteDirette;
	}
	
	public void setPartiteDirette(List<Partita> partiteDirette) {
		this.partiteDirette=partiteDirette;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(codice_arbitrale);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arbitro other = (Arbitro) obj;
		return Objects.equals(codice_arbitrale, other.codice_arbitrale);
	}


}
