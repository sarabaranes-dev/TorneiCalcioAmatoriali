package it.uniroma3.siw.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.PartitaDTO;
import it.uniroma3.siw.service.PartitaService;

@RestController
@CrossOrigin(origins = "http://localhost:5173") 
public class PartitaRestController {

    @Autowired 
    private PartitaService partitaService;

    @GetMapping("/rest/partite")
    public List<PartitaDTO> getPartiteRest() {
        Iterable<Partita> partite = partitaService.findAll(); 
        
        List<PartitaDTO> partiteDTO = new ArrayList<>();
        for (Partita p : partite) {
            partiteDTO.add(new PartitaDTO(p));
        }
        return partiteDTO;
    }
    
}