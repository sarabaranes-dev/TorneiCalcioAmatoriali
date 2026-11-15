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
                    .header(HttpHeaders.CONTENT_TYPE, tipoImmagine(squadra.getLogo())) 
                    .body(squadra.getLogo());
        }
        return ResponseEntity.notFound().build();
    }

    // riconosce PNG dai primi byte del file, altrimenti JPEG (formato usato finora)
    private static String tipoImmagine(byte[] dati) {
        if (dati.length > 4 && (dati[0] & 0xFF) == 0x89 && dati[1] == 'P' && dati[2] == 'N' && dati[3] == 'G') {
            return "image/png";
        }
        return "image/jpeg";
    }
}