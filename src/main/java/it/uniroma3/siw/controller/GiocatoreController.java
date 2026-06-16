package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.GiocatoreValidator;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.SquadraService;
import jakarta.validation.Valid;

@Controller
public class GiocatoreController {
    
    @Autowired
    private GiocatoreService giocatoreService;
    
    @Autowired
    private GiocatoreValidator giocatoreValidator;

    @Autowired
    private SquadraService squadraService;


    @GetMapping("/giocatori")
    public String getGiocatori(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model) {
        
        int pageSize = 5; 
        
        Page<Giocatore> pageGiocatori = this.giocatoreService.getGiocatoriCercatiEPaginati(keyword, page, pageSize);
        
        model.addAttribute("giocatori", pageGiocatori.getContent());
        
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageGiocatori.getTotalPages());
        model.addAttribute("keyword", keyword); 
        
        return "giocatori"; 
    }
    

    @GetMapping("/giocatori/{id}")
    public String getGiocatore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("giocatore", this.giocatoreService.findById(id));
        return "giocatore"; 
    }

    /*FUNZIONALITA RISERVATE ALL'ADMIN*/
    
    /**
     * CASO D'USO: "inserimento di un giocatore"
     */
    
    @GetMapping(value="/admin/formNuovoGiocatore")
    public String formNuovaSquadra(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", this.squadraService.findAll());
        return "admin/formNuovoGiocatore";
    }
    
    //salvo giocatore nel database
    @GetMapping(value="/admin/giocatori")
    public String getGiocatoriAdmin(@RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     Model model) {
        int pageSize = 5; 
        
        Page<Giocatore> pageGiocatori = this.giocatoreService.getGiocatoriCercatiEPaginati(keyword, page, pageSize);
        
        model.addAttribute("giocatori", pageGiocatori.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageGiocatori.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "admin/listaGiocatori";
    }

    @PostMapping("/admin/giocatori")
    public String nuovoGiocatore(@Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult bindingResult, Model model) {
        
        this.giocatoreValidator.validate(giocatore, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.giocatoreService.saveGiocatore(giocatore); 
            return "redirect:/giocatori/" + giocatore.getId(); 
        } else {
            model.addAttribute("squadre", this.squadraService.findAll());
            return "admin/formNuovoGiocatore"; 
        }
    }
    
    /**
     * CASO D'USO: "modifica di un giocatore"
     */
    @GetMapping(value="/admin/formModificaGiocatore/{id}")
    public String formModificaGiocatore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("giocatore", giocatoreService.findById(id));
        model.addAttribute("squadre", this.squadraService.findAll());
        return "admin/formModificaGiocatore"; // Corretto: senza .html
    }


    @PostMapping("/admin/giocatori/modifica/{id}")
    public String modificaGiocatore(@PathVariable("id") Long id, @Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            this.giocatoreService.saveGiocatore(giocatore);
            return "redirect:/giocatori/" + id;
        } else {
            model.addAttribute("squadre", this.squadraService.findAll());
            return "admin/formModificaGiocatore"; // Corretto: senza .html
        }
    }
}