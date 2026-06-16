package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.PartitaValidator;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.service.ArbitroService;
import it.uniroma3.siw.service.PartitaService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.TorneoService;
import jakarta.validation.Valid;

@Controller
public class PartitaController {

	@Autowired 
	private PartitaService partitaService;
	
	@Autowired 
	private SquadraService squadraService;
	
	@Autowired 
	private TorneoService torneoService;

	@Autowired 
	private ArbitroService arbitroService;
	

	@Autowired
	private PartitaValidator partitaValidator;
	
	/*FUNZIONALITA PUBBLICHE*/
	
	/**
	 * CASO D'USO: "Visualizzazione della lista di tutte le partite"
	 */
	@GetMapping("/partite")
	public String getPartite(Model model) {
		model.addAttribute("partite", this.partitaService.findAll());
		return "partite.html";
	}
	
	/**
	 * CASO D'USO: "Visualizzazione del dettaglio di una singola partita"
	 */
	@GetMapping("/partite/{id}")
	public String getPartita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("partita", this.partitaService.findById(id));
		return "partita.html";
	}
	

	/*FUNZIONALITA RISERVATE ALL'ADMIN*/
	
	/**
     * CASO D'USO: "registrazione di una nuova partita"
     */
	
	@GetMapping(value="/admin/formNuovaPartita")
	public String formNuovaPartita(Model model) {
		model.addAttribute("partita", new Partita());
		model.addAttribute("squadre", this.squadraService.findAll());
		model.addAttribute("tornei", this.torneoService.findAll());
		model.addAttribute("arbitri", this.arbitroService.findAll());
		return "admin/formNuovaPartita.html";
	}
	
	//salvo nuova partita nel database
	@PostMapping("/admin/partite")
    public String nuovaPartita(@Valid @ModelAttribute("partita") Partita partita, BindingResult bindingResult, Model model) {
        
        this.partitaValidator.validate(partita, bindingResult);
        if (!bindingResult.hasErrors()) {
            
            //controllo sulla data
            if (partita.getDataEora() != null && partita.getDataEora().isBefore(java.time.LocalDateTime.now())) {
                //se la data inserita è nel passato, la partita è già stata giocata
                partita.setStato(Partita.StatoPartita.PLAYED);
            } else {
                //altrimenti è in programma per il futuro
                partita.setStato(Partita.StatoPartita.SCHEDULED);
            }
            
            this.partitaService.savePartita(partita); 
            return "redirect:/partite/"+partita.getId();
        } else {
            model.addAttribute("squadre", this.squadraService.findAll());
            model.addAttribute("tornei", this.torneoService.findAll());
            model.addAttribute("arbitri", this.arbitroService.findAll());
            return "admin/formNuovaPartita.html"; 
        }
    }
	
	/**
     * CASO D'USO: "inserimento del risultato di una partita"
     */
	
	@GetMapping(value="/admin/partite")
	public String getPartiteAdmin(Model model) {
		model.addAttribute("partite", this.partitaService.findAll());
		return "admin/listaPartite.html";
	}

	@GetMapping(value="/admin/formRisultatoPartita/{id}")
	public String formRisultatoPartita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("partita", this.partitaService.findById(id));
		return "admin/formRisultatoPartita.html";
	}
	
	/**
     salvataggio
     */
	@PostMapping("/admin/salvaRisultato/{id}")
    public String salvaRisultato(@PathVariable("id") Long id, @ModelAttribute("partita") Partita partitaForm) {
        this.partitaService.aggiornaRisultato(id, partitaForm.getGoalsHome(), partitaForm.getGoalsAway());
        
        return "redirect:/partite/" + id;
    }
	
	/**
     * CASO D'USO: "eliminazione di una partita"
     */
	@GetMapping(value="/admin/eliminaPartita/{id}")
	public String eliminaPartita(@PathVariable("id") Long id) {
		Partita partita = this.partitaService.findById(id);
		Long torneoId = partita.getTorneo().getId();
		
		this.partitaService.deletePartita(id);
		
		return "redirect:/tornei/" + torneoId;
	}
}
