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
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.PartitaService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.TorneoService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
    
    @Autowired
    private CredenzialiService credenzialiService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private GiocatoreService giocatoreService;

    @Autowired
    private PartitaService partitaService;
    
    
    @GetMapping(value = "/login") 
    public String showLoginForm (Model model) {
        return "formLogin";
    }

    @GetMapping(value = "/") 
	public String index(Model model) {
		// numeri mostrati nella home
		model.addAttribute("numTornei", this.torneoService.count());
		model.addAttribute("numSquadre", this.squadraService.count());
		model.addAttribute("numGiocatori", this.giocatoreService.count());
		model.addAttribute("numPartite", this.partitaService.count());
		// la home pubblica e' uguale per tutti: l'admin raggiunge il pannello da "Area admin"
		return "index.html";
	}
        
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());
        if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
            return "redirect:/admin/indexAdmin";
        }
        return "redirect:/";
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
        Credenziali credenziali = new Credenziali();
        //lego utente dentro le credenziali
        credenziali.setUtente(new Utente());
        
        //passo solo oggetto credenziali che contiene tutto
        model.addAttribute("credenziali", credenziali);
        return "formRegistrazioneUtente";
    }

    @PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("credenziali") Credenziali credenziali,
                 BindingResult credentialsBindingResult,
                 Model model) {

        // username gia' usato: errore sul campo invece di un errore del database
        if (credenziali.getUsername() != null
                && this.credenzialiService.getCredenziali(credenziali.getUsername()) != null) {
            credentialsBindingResult.rejectValue("username", "credenziali.duplicate");
        }

        if (!credentialsBindingResult.hasErrors()) {
            
            Utente utente = credenziali.getUtente();
            this.utenteService.saveUtente(utente);
            
            credenziali.setRuolo(Credenziali.USER_ROLE);
            this.credenzialiService.saveCredenziali(credenziali);
        
            return "redirect:/login?registrato";
        }
        
        return "formRegistrazioneUtente";
    }

	
}