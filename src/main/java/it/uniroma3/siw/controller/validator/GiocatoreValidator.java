package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.service.GiocatoreService;
import org.springframework.validation.Validator;

@Component
public class GiocatoreValidator implements Validator {
    
    @Autowired
    private GiocatoreService giocatoreService; 

    public boolean supports(Class<?> aClass) {
        return Giocatore.class.equals(aClass); 
    }

    public void validate(Object o, Errors errors) {
        Giocatore giocatore = (Giocatore) o;
        
        //controllo nome, cognome, data di nascita 
        if (giocatore.getNome() != null && giocatore.getCognome() != null && giocatore.getDataNascita() != null 
                && giocatoreService.existsByNomeAndCognomeAndDataNascita(giocatore.getNome(), giocatore.getCognome(), giocatore.getDataNascita())) {
            errors.reject("giocatore.duplicate"); 
        }
    }
}
