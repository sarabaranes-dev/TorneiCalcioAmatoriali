package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.service.TorneoService;
import org.springframework.validation.Validator;

@Component
public class TorneoValidator implements Validator{
	
	@Autowired
	public TorneoService torneoService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Torneo torneo = (Torneo)o;
		if (torneo.getNome()!=null && torneo.getAnno()!=null 
				&& torneoService.existsByNomeAndAnno(torneo.getNome(), torneo.getAnno())) {
			errors.reject("torneo.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Torneo.class.equals(aClass);
	}
	
}