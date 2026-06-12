package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
    
    @Autowired
    private CredenzialiService credenzialiService;

    @Autowired
    private UtenteService utenteService;
    
    
    @GetMapping(value = "/login") 
    public String showLoginForm (Model model) {
        return "formLogin";
    }

    @GetMapping(value = "/") 
	public String index(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// SE l'autenticazione è nulla OPPURE è un utente anonimo, mostra la index pubblica
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "index.html";
		}
		else {      
			// Ora siamo SICURI che c'è un utente loggato, quindi questo cast non fallirà mai
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());
			
			if (credenziali != null && credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			}
		}
		return "index.html";
	}
        
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());
        if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
        return "index.html";
    }

    @GetMapping(value = "/admin/indexAdmin")
    public String adminDashboard(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());
        if (credenziali != null && credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
        return "redirect:/";
    }

	@GetMapping(value = "/register") 
    public String showRegisterForm (Model model) {
        // Creiamo l'oggetto Credenziali
        Credenziali credenziali = new Credenziali();
        // Leghiamo l'Utente dentro le credenziali (relazione 1to1)
        credenziali.setUtente(new Utente());
        
        // Passiamo solo l'oggetto "credenziali" che ora contiene tutto
        model.addAttribute("credenziali", credenziali);
        return "formRegistrazioneUtente";
    }

    @PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("credenziali") Credenziali credenziali,
                 BindingResult credentialsBindingResult,
                 Model model) {

        if (!credentialsBindingResult.hasErrors()) {
            
            Utente utente = credenziali.getUtente();
            this.utenteService.saveUtente(utente);
            
            credenziali.setRuolo(Credenziali.USER_ROLE);
            this.credenzialiService.saveCredenziali(credenziali);
        
            return "registrazioneAvvenuta";
        }
        
        return "formRegistrazioneUtente";
    }

	
}