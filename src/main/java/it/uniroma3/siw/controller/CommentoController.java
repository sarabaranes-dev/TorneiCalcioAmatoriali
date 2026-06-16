package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.PartitaService;

@Controller
public class CommentoController {

	@Autowired
	private CommentoService commentoService;

	@Autowired
	private CredenzialiService credenzialiService;
	
	@Autowired
	private PartitaService partitaService;

	/*FUNZIONALITA RISERVATE AGLI UTENTI REGISTRATI*/
	
	
	/**
	 * CASO D'USO: "visualizzazione commenti"
	 */
	@GetMapping("/utente/partite/{partitaId}/commenti")
	public String visualizzaCommenti(@PathVariable("partitaId") Long partitaId,
							 @AuthenticationPrincipal UserDetails userDetails,
							 Model model) {
		
		Partita partita = this.partitaService.findById(partitaId);
		Iterable<Commento> listaCommenti = this.commentoService.findByPartitaCommentataId(partitaId);
		Credenziali credenzialiLoggato = this.credenzialiService.getCredenziali(userDetails.getUsername());
		Long currentUserId = credenzialiLoggato.getUtente().getId();
		
		//passo dati alla pagina html
		model.addAttribute("partita", partita);
		model.addAttribute("commenti", listaCommenti);
		model.addAttribute("currentUserId", currentUserId);
		
		//preparo oggetto vuoto per il form di inserimento
		model.addAttribute("nuovoCommento", new Commento());
		
		return "utente/commenti.html";
	}
	
	/**
     * CASO D'USO: "inserimento di un commento"
     */
	
	@PostMapping("/utente/partite/{partitaId}/commenti")
	public String inserisciCommento(@PathVariable("partitaId") Long partitaId,
	                                @ModelAttribute("nuovoCommento") Commento commentoForm,
	                                @AuthenticationPrincipal UserDetails userDetails) {
		
		Partita partita = this.partitaService.findById(partitaId);
		Credenziali credenzialiLoggato = this.credenzialiService.getCredenziali(userDetails.getUsername());
		Utente autore = credenzialiLoggato.getUtente();
		
		//collego commento alla partita corretta
		commentoForm.setPartitaCommentata(partita);
		
		this.commentoService.inserisciCommento(commentoForm, autore);
	
		return "redirect:/utente/partite/" + partitaId + "/commenti";
	}
	
	
	/**
	 * CASO D'USO: Modifica del proprio commento
	 */
	
	@PostMapping("/utente/commenti/modifica/{id}")
    public String modificaCommento(@PathVariable("id") Long id, 
                                   @RequestParam("testo") String nuovoTesto,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        
        Commento commentoOriginale = this.commentoService.findById(id);
        Credenziali credenzialiLoggato = this.credenzialiService.getCredenziali(userDetails.getUsername());
        
        //controllo autore
        if (commentoOriginale.getAutore() == null || commentoOriginale.getAutore().getId() == null
                || !commentoOriginale.getAutore().getId().equals(credenzialiLoggato.getUtente().getId())) {
            return "accessoNegato.html"; 
        }
        
        //passo il testo estratto dal form
        this.commentoService.modificaCommento(id, nuovoTesto, credenzialiLoggato.getUtente());
        
        return "redirect:/utente/partite/" + commentoOriginale.getPartitaCommentata().getId() + "/commenti";
    }
	
}
