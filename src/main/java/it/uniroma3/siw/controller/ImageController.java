package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;

@Controller
public class ImageController {

    @Autowired
    private SquadraService squadraService;

    @GetMapping("/squadre/{id}/logo")
    public ResponseEntity<byte[]> getSquadraLogo(@PathVariable("id") Long id) {
        Squadra squadra = this.squadraService.findById(id);
        
        if (squadra != null && squadra.getLogo() != null && squadra.getLogo().length > 0) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Funziona benissimo anche per PNG
                    .body(squadra.getLogo());
        }
        return ResponseEntity.notFound().build();
    }
}