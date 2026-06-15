package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.transaction.Transactional;
import it.uniroma3.siw.controller.validator.TorneoValidator;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.service.TorneoService;
import jakarta.validation.Valid;

@Controller
public class TorneoController {

	@Autowired
	private TorneoService torneoService;
	
	@Autowired
	private TorneoValidator torneoValidator;
	
	/*FUNZIONALITA PUBBLICHE*/
	
	
	/**
     * CASO D'USO: "visualizzazione dell’elenco dei tornei"
     * URL: http://localhost:8080/tornei
     */
	@GetMapping("/tornei")
	public String getTornei(Model model) {
		// Prendo tutti i tornei dal database e li mette nel model con la chiave "tornei"
		model.addAttribute("tornei",this.torneoService.findAll());
		return "tornei.html"; // restituisce il file src/main/resources/templates/tornei.html
	}
	

	/**
     * CASI D'USO: 
     * "visualizzazione del dettaglio di un torneo"
     * "visualizzazione delle squadre partecipanti"
     * "visualizzazione del calendario delle partite"
     * "visualizzazione della classifica del torneo"
     * URL: http://localhost:8080/tornei/1
     */

	@Transactional
	@GetMapping("/tornei/{id}")
    public String getTorneo(@PathVariable("id") Long id, Model model) {
        model.addAttribute("torneo", this.torneoService.findByIdWithPartite(id));
        model.addAttribute("classifica", this.torneoService.getClassificaTorneo(id));
        return "torneo.html";
    }
	
	/*FUNZIONALITA RISERVATE ALL'ADMIN*/
	
	/**
     * CASO D'USO: "creazione di un torneo"
     */
	
	@GetMapping(value="/admin/formNuovoTorneo")
	public String formNuovoTorneo(Model model) {
		model.addAttribute("torneo", new Torneo());
		return "admin/formNuovoTorneo.html";
	}
	
	// Salva il nuovo torneo nel database
	
	@GetMapping(value="/admin/tornei")
	public String getTorneiAdmin(Model model) {
		model.addAttribute("tornei", this.torneoService.findAll());
		return "admin/listaTornei.html";
	}

	@PostMapping("/admin/tornei")
	public String nuovoTorneo(@Valid @ModelAttribute("torneo") Torneo torneo, BindingResult bindingResult, Model model) {
		
		this.torneoValidator.validate(torneo, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.torneoService.saveTorneo(torneo); 
			model.addAttribute("torneo", torneo);
			return "redirect:/tornei/"+torneo.getId();
		} else {
			return "admin/formNuovoTorneo.html"; 
		}
	}
	
	/**
     * CASO D'USO: "modifica di un torneo"
     */
	@GetMapping(value="/admin/formModificaTorneo/{id}")
	public String formModificaTorneo(@PathVariable("id") Long id, Model model) {
		model.addAttribute("torneo", torneoService.findById(id));
		return "admin/formModificaTorneo.html";
	}

	@PostMapping("/admin/tornei/modifica/{id}")
	public String modificaTorneo(@PathVariable("id") Long id, @Valid @ModelAttribute("torneo") Torneo torneo, BindingResult bindingResult, Model model) {
		
		if (!bindingResult.hasErrors()) {
			this.torneoService.saveTorneo(torneo);
			return "redirect:/tornei/" + id;
		} else {
			model.addAttribute("torneo", torneo);
			return "admin/formModificaTorneo.html";
		}
	}

    
	
	
}
