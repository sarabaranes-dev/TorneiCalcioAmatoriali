package it.uniroma3.siw.controller.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.service.PartitaService;

@Component
public class PartitaValidator implements Validator {

	@Autowired
	private PartitaService partitaService; 

	@Override
	public boolean supports(Class<?> aClass) {
		return Partita.class.equals(aClass); 
	}

	@Override
	public void validate(Object o, Errors errors) {
		Partita partita = (Partita) o;
		
		//verifica che l'admin abbia selezionato tutti i dati obbligatori nel form
		if (partita.getTorneo() != null && partita.getSquadraCasa() != null 
				&& partita.getSquadraOspite() != null && partita.getDataEora() != null) {
			
			// una squadra non può giocare contro se stessa
			if (partita.getSquadraCasa().equals(partita.getSquadraOspite())) {
				errors.reject("partita.stessaSquadra"); 
			}
			
			// controllo che non esista stesso torneo, stesse squadre, stessa data e ora
			if (this.partitaService.existsByTorneoAndSquadraCasaAndSquadraOspiteAndDataEora(
					partita.getTorneo(), partita.getSquadraCasa(), partita.getSquadraOspite(), partita.getDataEora())) {
				errors.reject("partita.duplicate"); 
			}
		}
	}
}
