package it.uniroma3.siw.controller.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;

@Component
public class SquadraValidator implements Validator {
    
    @Autowired
    private SquadraService squadraService; 

    @Override
    public boolean supports(Class<?> aClass) {
        return Squadra.class.equals(aClass); 
    }

    @Override
    public void validate(Object o, Errors errors) {
        Squadra squadra = (Squadra) o;
        
        if (squadra.getNome() != null) {
            Optional<Squadra> existing = this.squadraService.findByNome(squadra.getNome());
            if (existing.isPresent() && !existing.get().getId().equals(squadra.getId())) {
                errors.reject("squadra.duplicate");
            }
        }
    }
}

