package it.uniroma3.siw.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    // elemento non trovato (es. /squadre/999): pagina 404 invece di un errore generico
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String elementoNonTrovato(Model model) {
        model.addAttribute("status", 404);
        return "error";
    }
    
    @ModelAttribute("utenteLoggato")
    public UserDetails getUtente() {
        UserDetails utente = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
        	utente = (UserDetails) authentication.getPrincipal();
        }
        return utente;
    }
}
