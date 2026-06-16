package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.SquadraValidator;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;
import jakarta.validation.Valid;


@Controller
public class SquadraController {

	@Autowired
	public SquadraService squadraService;
	

	@Autowired
	private SquadraValidator squadraValidator;
	
    /*FUNZIONALITA PUBBLICHE*/
	
	/**
     * CASO D'USO: "visualizzazione delle squadre"
     */
	@GetMapping("/squadre")
	public String getSquadre(Model model) {
		model.addAttribute("squadre", this.squadraService.findAll());
		return "squadre.html";
	}
	
	/**
     * CASO D'USO: "visualizzazione del dettaglio di una squadra (con giocatori)"
     */
	@GetMapping("/squadre/{id}")
	public String getSquadra(@PathVariable("id") Long id, Model model) {
		model.addAttribute("squadra", this.squadraService.findById(id));
		return "squadra.html";
	}
	
	
    /*FUNZIONALITA RISERVATE ALL'ADMIN*/
	
	/**
     * CASO D'USO: "inserimento di una squadra"
     */
	
	@GetMapping(value="/admin/formNuovaSquadra")
	public String formNuovaSquadra(Model model) {
		model.addAttribute("squadra", new Squadra());
		return "admin/formNuovaSquadra.html";
	}
	
	//salvo la squadra nel database
	
	@GetMapping(value="/admin/squadre")
	public String getSquadreAdmin(Model model) {
		model.addAttribute("squadre", this.squadraService.findAll());
		return "admin/listaSquadre.html";
	}
	
	@PostMapping("/admin/squadre")
    public String nuovaSquadra(@Valid @ModelAttribute("squadra") Squadra squadra, 
                               BindingResult bindingResult, 
                               @RequestParam("fileLogo") MultipartFile fileLogo, 
                               Model model) {
        this.squadraValidator.validate(squadra, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                //passo squadra e  file al service
                this.squadraService.saveSquadra(squadra, fileLogo); 
                return "redirect:/admin/squadre";
            } catch (IOException e) {
                model.addAttribute("erroreFile", "Errore nel caricamento del logo dell'immagine.");
                return "admin/formNuovaSquadra.html";
            }
        } else {
            model.addAttribute("squadra", squadra);
            return "admin/formNuovaSquadra.html"; 
        }
    }
	
	/**
     * CASO D'USO: "modifica di una squadra"
     */
	@GetMapping(value="/admin/formModificaSquadra/{id}")
	public String formModificaSquadra(@PathVariable("id") Long id, Model model) {
		model.addAttribute("squadra", squadraService.findById(id));
		return "admin/formModificaSquadra.html";
	}

	@PostMapping("/admin/squadre/modifica/{id}")
    public String modificaSquadra(@PathVariable("id") Long id, 
                                  @Valid @ModelAttribute("squadra") Squadra squadra, 
                                  BindingResult bindingResult, 
                                  @RequestParam("fileLogo") MultipartFile fileLogo,
                                  Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                //passo squadra e file al service
                this.squadraService.saveSquadra(squadra, fileLogo);
                return "redirect:/admin/squadre";
            } catch (IOException e) {
                model.addAttribute("erroreFile", "Errore nel caricamento del logo dell'immagine.");
                return "admin/formModificaSquadra.html";
            }
        } else {
            model.addAttribute("squadra", squadra);
            return "admin/formModificaSquadra.html";
        }
    }

	/**
     * CASO D'USO: "eliminazione di una squadra"
     */
    @GetMapping(value="/admin/eliminaSquadra/{id}")
    public String eliminaSquadra(@PathVariable("id") Long id, Model model) {
        try {
            this.squadraService.deleteSquadra(id);
        } catch (Exception ex) { 
            model.addAttribute("deleteError", "Impossibile eliminare la squadra: è ancora collegata a tornei, partite o partecipazioni.");
            model.addAttribute("squadre", this.squadraService.findAll());
            return "admin/listaSquadre.html"; 
        }
        return "redirect:/admin/squadre";
    }


}
